// Generated from R5RS.g4 by ANTLR 4.9.2

    package com.ihorak.truffle.parser.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class R5RSLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, NUMBER=7, STRING=8, BOOLEAN=9, 
		SYMBOL=10, WS=11, COMMENT=12;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "NUMBER", "STRING", "BOOLEAN", 
			"SYMBOL", "WS", "COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'''", "'`'", "','", "',@'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, "NUMBER", "STRING", "BOOLEAN", 
			"SYMBOL", "WS", "COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public R5RSLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "R5RS.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\16R\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3"+
		"\7\3\b\5\b*\n\b\3\b\6\b-\n\b\r\b\16\b.\3\t\3\t\3\t\3\t\7\t\65\n\t\f\t"+
		"\16\t8\13\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\7\13A\n\13\f\13\16\13D\13\13"+
		"\3\f\3\f\3\f\3\f\3\r\3\r\7\rL\n\r\f\r\16\rO\13\r\3\r\3\r\2\2\16\3\3\5"+
		"\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\3\2\t\3\2\62;\3\2"+
		"$$\6\2HHVVhhvv\n\2\13\f\17\17\"\"$%)+..BBbb\n\2\13\f\17\17\"\"$$)+..B"+
		"Bbb\5\2\13\f\17\17\"\"\4\2\f\f\17\17\2W\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\3\33\3\2\2\2\5\35"+
		"\3\2\2\2\7\37\3\2\2\2\t!\3\2\2\2\13#\3\2\2\2\r%\3\2\2\2\17)\3\2\2\2\21"+
		"\60\3\2\2\2\23;\3\2\2\2\25>\3\2\2\2\27E\3\2\2\2\31I\3\2\2\2\33\34\7*\2"+
		"\2\34\4\3\2\2\2\35\36\7+\2\2\36\6\3\2\2\2\37 \7)\2\2 \b\3\2\2\2!\"\7b"+
		"\2\2\"\n\3\2\2\2#$\7.\2\2$\f\3\2\2\2%&\7.\2\2&\'\7B\2\2\'\16\3\2\2\2("+
		"*\7/\2\2)(\3\2\2\2)*\3\2\2\2*,\3\2\2\2+-\t\2\2\2,+\3\2\2\2-.\3\2\2\2."+
		",\3\2\2\2./\3\2\2\2/\20\3\2\2\2\60\66\7$\2\2\61\65\n\3\2\2\62\63\7^\2"+
		"\2\63\65\7$\2\2\64\61\3\2\2\2\64\62\3\2\2\2\658\3\2\2\2\66\64\3\2\2\2"+
		"\66\67\3\2\2\2\679\3\2\2\28\66\3\2\2\29:\7$\2\2:\22\3\2\2\2;<\7%\2\2<"+
		"=\t\4\2\2=\24\3\2\2\2>B\n\5\2\2?A\n\6\2\2@?\3\2\2\2AD\3\2\2\2B@\3\2\2"+
		"\2BC\3\2\2\2C\26\3\2\2\2DB\3\2\2\2EF\t\7\2\2FG\3\2\2\2GH\b\f\2\2H\30\3"+
		"\2\2\2IM\7=\2\2JL\n\b\2\2KJ\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2NP\3"+
		"\2\2\2OM\3\2\2\2PQ\b\r\2\2Q\32\3\2\2\2\t\2).\64\66BM\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}