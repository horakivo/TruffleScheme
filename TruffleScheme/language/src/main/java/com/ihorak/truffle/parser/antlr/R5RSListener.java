// Generated from R5RS.g4 by ANTLR 4.9.2

    package com.ihorak.truffle.parser.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link R5RSParser}.
 */
public interface R5RSListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code Program}
	 * labeled alternative in {@link R5RSParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProgram(R5RSParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Program}
	 * labeled alternative in {@link R5RSParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProgram(R5RSParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link R5RSParser#form}.
	 * @param ctx the parse tree
	 */
	void enterForm(R5RSParser.FormContext ctx);
	/**
	 * Exit a parse tree produced by {@link R5RSParser#form}.
	 * @param ctx the parse tree
	 */
	void exitForm(R5RSParser.FormContext ctx);
	/**
	 * Enter a parse tree produced by {@link R5RSParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(R5RSParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link R5RSParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(R5RSParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link R5RSParser#quote}.
	 * @param ctx the parse tree
	 */
	void enterQuote(R5RSParser.QuoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link R5RSParser#quote}.
	 * @param ctx the parse tree
	 */
	void exitQuote(R5RSParser.QuoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link R5RSParser#quasiquote}.
	 * @param ctx the parse tree
	 */
	void enterQuasiquote(R5RSParser.QuasiquoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link R5RSParser#quasiquote}.
	 * @param ctx the parse tree
	 */
	void exitQuasiquote(R5RSParser.QuasiquoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link R5RSParser#unquote}.
	 * @param ctx the parse tree
	 */
	void enterUnquote(R5RSParser.UnquoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link R5RSParser#unquote}.
	 * @param ctx the parse tree
	 */
	void exitUnquote(R5RSParser.UnquoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link R5RSParser#unquote_splicing}.
	 * @param ctx the parse tree
	 */
	void enterUnquote_splicing(R5RSParser.Unquote_splicingContext ctx);
	/**
	 * Exit a parse tree produced by {@link R5RSParser#unquote_splicing}.
	 * @param ctx the parse tree
	 */
	void exitUnquote_splicing(R5RSParser.Unquote_splicingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNumber(R5RSParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNumber(R5RSParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(R5RSParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(R5RSParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterString(R5RSParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitString(R5RSParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Symbol}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterSymbol(R5RSParser.SymbolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Symbol}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitSymbol(R5RSParser.SymbolContext ctx);
}