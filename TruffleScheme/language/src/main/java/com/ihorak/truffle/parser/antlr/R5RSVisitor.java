// Generated from R5RS.g4 by ANTLR 4.9.2

    package com.ihorak.truffle.parser.antlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link R5RSParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface R5RSVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code Program}
	 * labeled alternative in {@link R5RSParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(R5RSParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link R5RSParser#form}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForm(R5RSParser.FormContext ctx);
	/**
	 * Visit a parse tree produced by {@link R5RSParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(R5RSParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link R5RSParser#quote}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuote(R5RSParser.QuoteContext ctx);
	/**
	 * Visit a parse tree produced by {@link R5RSParser#quasiquote}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuasiquote(R5RSParser.QuasiquoteContext ctx);
	/**
	 * Visit a parse tree produced by {@link R5RSParser#unquote}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnquote(R5RSParser.UnquoteContext ctx);
	/**
	 * Visit a parse tree produced by {@link R5RSParser#unquote_splicing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnquote_splicing(R5RSParser.Unquote_splicingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(R5RSParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(R5RSParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(R5RSParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Symbol}
	 * labeled alternative in {@link R5RSParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymbol(R5RSParser.SymbolContext ctx);
}