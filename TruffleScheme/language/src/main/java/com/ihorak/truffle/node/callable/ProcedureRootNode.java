package com.ihorak.truffle.node.callable;

import java.util.List;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.SchemeTruffleLanguage.TCOTarget;
import com.ihorak.truffle.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.api.profiles.ConditionProfile;

public class ProcedureRootNode extends RootNode {

	@Children
	private final SchemeExpression[] expressions;

	private final SchemeSymbol name;
	private final ConditionProfile tailRecursion = ConditionProfile.create();
	@Child private DispatchNode dispatchNode = DispatchNodeGen.create();

	@Child
	private LoopNode loop;

	private final int argumentsIndex;

	public ProcedureRootNode(SchemeSymbol name, TruffleLanguage<?> language, FrameDescriptor frameDescriptor,
			List<SchemeExpression> schemeExpressions, int argumentsIndex) {
		super(language, frameDescriptor);
		this.expressions = schemeExpressions.toArray(SchemeExpression[]::new);
		this.name = name;
		this.argumentsIndex = argumentsIndex;
		this.loop = Truffle.getRuntime().createLoopNode(new RecursiveTailCallLoopNode());
	}

	@Override
	public String getName() {
		return name.getValue();
	}

	@Override
	public String toString() {
		return name.getValue();
	}

	@Override
	public Object execute(VirtualFrame frame) {
		frame.setObject(argumentsIndex, frame.getArguments());
		return loop.execute(frame);
	}

	@ExplodeLoop
	private Object executeImpl(VirtualFrame frame) {
		for (int i = 0; i < expressions.length - 1; i++) {
			expressions[i].executeGeneric(frame);
		}
		// return last element
		return expressions[expressions.length - 1].executeGeneric(frame);
	}

	final class RecursiveTailCallLoopNode extends SchemeNode implements RepeatingNode {

		@Override
		public boolean executeRepeating(final VirtualFrame frame) {
			throw CompilerDirectives.shouldNotReachHere();
		}

		@Override
		public Object executeRepeatingWithValue(final VirtualFrame frame) {
			try {
				Object[] arguments = (Object[]) frame.getObject(argumentsIndex);
				var framee = Truffle.getRuntime().createVirtualFrame(arguments, getFrameDescriptor());
				//return dispatchNode.executeDispatch(getCallTarget(), arguments);
				return executeImpl(framee);
			} catch (TailCallException e) {
//				frame.setObject(argumentsIndex, e.getArguments());
//				return CONTINUE_LOOP_STATUS;

				TCOTarget target = SchemeTruffleLanguage.getTCOTarget(this);
				frame.setObject(argumentsIndex, target.arguments);
				return CONTINUE_LOOP_STATUS;

//				if (tailRecursion.profile(target.target == getCallTarget())) {
//					frame.setObject(argumentsIndex, e.getArguments());
//					return CONTINUE_LOOP_STATUS;
//				} else {
//					throw e;
//				}
			}
		}

		@Override
		public boolean shouldContinue(final Object returnValue) {
			return returnValue == CONTINUE_LOOP_STATUS;
		}
	}
}
