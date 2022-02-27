// Generated from R5RS.g4 by ANTLR 4.9.2

    package com.ihorak.truffle.parser.antlr;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class R5RSLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, NUMBER=7, FLOAT=8, STRING=9, 
		BOOLEAN=10, SYMBOL=11, WS=12, COMMENT=13;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "NUMBER", "FLOAT", "STRING", 
			"BOOLEAN", "SYMBOL", "DIGIT", "WS", "COMMENT"
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
			null, null, null, null, null, null, null, "NUMBER", "FLOAT", "STRING", 
			"BOOLEAN", "SYMBOL", "WS", "COMMENT"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\17g\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\7\3\b\5\b.\n\b\3\b\6\b\61\n\b\r\b\16\b\62\3\t\5\t"+
		"\66\n\t\3\t\6\t9\n\t\r\t\16\t:\3\t\3\t\7\t?\n\t\f\t\16\tB\13\t\3\n\3\n"+
		"\3\n\3\n\7\nH\n\n\f\n\16\nK\13\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\7\fT\n"+
		"\f\f\f\16\fW\13\f\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\7\17a\n\17\f\17"+
		"\16\17d\13\17\3\17\3\17\2\2\20\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13"+
		"\25\f\27\r\31\2\33\16\35\17\3\2\t\3\2$$\6\2HHVVhhvv\n\2\13\f\17\17\"\""+
		"$%)+..BBbb\n\2\13\f\17\17\"\"$$)+..BBbb\3\2\62;\5\2\13\f\17\17\"\"\4\2"+
		"\f\f\17\17\2n\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2"+
		"\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2"+
		"\27\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3\37\3\2\2\2\5!\3\2\2\2\7#\3\2\2"+
		"\2\t%\3\2\2\2\13\'\3\2\2\2\r)\3\2\2\2\17-\3\2\2\2\21\65\3\2\2\2\23C\3"+
		"\2\2\2\25N\3\2\2\2\27Q\3\2\2\2\31X\3\2\2\2\33Z\3\2\2\2\35^\3\2\2\2\37"+
		" \7*\2\2 \4\3\2\2\2!\"\7+\2\2\"\6\3\2\2\2#$\7)\2\2$\b\3\2\2\2%&\7b\2\2"+
		"&\n\3\2\2\2\'(\7.\2\2(\f\3\2\2\2)*\7.\2\2*+\7B\2\2+\16\3\2\2\2,.\7/\2"+
		"\2-,\3\2\2\2-.\3\2\2\2.\60\3\2\2\2/\61\5\31\r\2\60/\3\2\2\2\61\62\3\2"+
		"\2\2\62\60\3\2\2\2\62\63\3\2\2\2\63\20\3\2\2\2\64\66\7/\2\2\65\64\3\2"+
		"\2\2\65\66\3\2\2\2\668\3\2\2\2\679\5\31\r\28\67\3\2\2\29:\3\2\2\2:8\3"+
		"\2\2\2:;\3\2\2\2;<\3\2\2\2<@\7\60\2\2=?\5\31\r\2>=\3\2\2\2?B\3\2\2\2@"+
		">\3\2\2\2@A\3\2\2\2A\22\3\2\2\2B@\3\2\2\2CI\7$\2\2DH\n\2\2\2EF\7^\2\2"+
		"FH\7$\2\2GD\3\2\2\2GE\3\2\2\2HK\3\2\2\2IG\3\2\2\2IJ\3\2\2\2JL\3\2\2\2"+
		"KI\3\2\2\2LM\7$\2\2M\24\3\2\2\2NO\7%\2\2OP\t\3\2\2P\26\3\2\2\2QU\n\4\2"+
		"\2RT\n\5\2\2SR\3\2\2\2TW\3\2\2\2US\3\2\2\2UV\3\2\2\2V\30\3\2\2\2WU\3\2"+
		"\2\2XY\t\6\2\2Y\32\3\2\2\2Z[\t\7\2\2[\\\3\2\2\2\\]\b\16\2\2]\34\3\2\2"+
		"\2^b\7=\2\2_a\n\b\2\2`_\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2ce\3\2\2"+
		"\2db\3\2\2\2ef\b\17\2\2f\36\3\2\2\2\f\2-\62\65:@GIUb\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}