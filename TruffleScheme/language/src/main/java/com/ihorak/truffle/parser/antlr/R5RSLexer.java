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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, NUMBER=8, FLOAT=9, 
		STRING=10, BOOLEAN=11, SYMBOL=12, WS=13, COMMENT=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "NUMBER", "FLOAT", 
			"STRING", "BOOLEAN", "SYMBOL", "DIGIT", "WS", "COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'.'", "'''", "'`'", "','", "',@'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "NUMBER", "FLOAT", "STRING", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20k\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\3\3\3\3\4"+
		"\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\t\5\t\62\n\t\3\t\6\t\65\n\t"+
		"\r\t\16\t\66\3\n\5\n:\n\n\3\n\6\n=\n\n\r\n\16\n>\3\n\3\n\7\nC\n\n\f\n"+
		"\16\nF\13\n\3\13\3\13\3\13\3\13\7\13L\n\13\f\13\16\13O\13\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\r\3\r\7\rX\n\r\f\r\16\r[\13\r\3\16\3\16\3\17\3\17\3\17"+
		"\3\17\3\20\3\20\7\20e\n\20\f\20\16\20h\13\20\3\20\3\20\2\2\21\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\2\35\17\37\20\3\2"+
		"\t\3\2$$\6\2HHVVhhvv\n\2\13\f\17\17\"\"$%)+..BBbb\n\2\13\f\17\17\"\"$"+
		"$)+..BBbb\3\2\62;\5\2\13\f\17\17\"\"\4\2\f\f\17\17\2r\2\3\3\2\2\2\2\5"+
		"\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2"+
		"\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\3!\3\2\2\2\5#\3\2\2\2\7%\3\2\2\2\t\'\3\2\2\2\13"+
		")\3\2\2\2\r+\3\2\2\2\17-\3\2\2\2\21\61\3\2\2\2\239\3\2\2\2\25G\3\2\2\2"+
		"\27R\3\2\2\2\31U\3\2\2\2\33\\\3\2\2\2\35^\3\2\2\2\37b\3\2\2\2!\"\7*\2"+
		"\2\"\4\3\2\2\2#$\7+\2\2$\6\3\2\2\2%&\7\60\2\2&\b\3\2\2\2\'(\7)\2\2(\n"+
		"\3\2\2\2)*\7b\2\2*\f\3\2\2\2+,\7.\2\2,\16\3\2\2\2-.\7.\2\2./\7B\2\2/\20"+
		"\3\2\2\2\60\62\7/\2\2\61\60\3\2\2\2\61\62\3\2\2\2\62\64\3\2\2\2\63\65"+
		"\5\33\16\2\64\63\3\2\2\2\65\66\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\67"+
		"\22\3\2\2\28:\7/\2\298\3\2\2\29:\3\2\2\2:<\3\2\2\2;=\5\33\16\2<;\3\2\2"+
		"\2=>\3\2\2\2><\3\2\2\2>?\3\2\2\2?@\3\2\2\2@D\7\60\2\2AC\5\33\16\2BA\3"+
		"\2\2\2CF\3\2\2\2DB\3\2\2\2DE\3\2\2\2E\24\3\2\2\2FD\3\2\2\2GM\7$\2\2HL"+
		"\n\2\2\2IJ\7^\2\2JL\7$\2\2KH\3\2\2\2KI\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3"+
		"\2\2\2NP\3\2\2\2OM\3\2\2\2PQ\7$\2\2Q\26\3\2\2\2RS\7%\2\2ST\t\3\2\2T\30"+
		"\3\2\2\2UY\n\4\2\2VX\n\5\2\2WV\3\2\2\2X[\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z"+
		"\32\3\2\2\2[Y\3\2\2\2\\]\t\6\2\2]\34\3\2\2\2^_\t\7\2\2_`\3\2\2\2`a\b\17"+
		"\2\2a\36\3\2\2\2bf\7=\2\2ce\n\b\2\2dc\3\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3"+
		"\2\2\2gi\3\2\2\2hf\3\2\2\2ij\b\20\2\2j \3\2\2\2\f\2\61\669>DKMYf\3\b\2"+
		"\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}