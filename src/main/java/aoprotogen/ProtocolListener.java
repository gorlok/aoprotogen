// Generated from Protocol.g4 by ANTLR 4.7.2

    package aoprotogen;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ProtocolParser}.
 */
public interface ProtocolListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(ProtocolParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(ProtocolParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(ProtocolParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(ProtocolParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(ProtocolParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(ProtocolParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(ProtocolParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(ProtocolParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#params}.
	 * @param ctx the parse tree
	 */
	void enterParams(ProtocolParser.ParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#params}.
	 * @param ctx the parse tree
	 */
	void exitParams(ProtocolParser.ParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(ProtocolParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(ProtocolParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#tuple}.
	 * @param ctx the parse tree
	 */
	void enterTuple(ProtocolParser.TupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#tuple}.
	 * @param ctx the parse tree
	 */
	void exitTuple(ProtocolParser.TupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#size}.
	 * @param ctx the parse tree
	 */
	void enterSize(ProtocolParser.SizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#size}.
	 * @param ctx the parse tree
	 */
	void exitSize(ProtocolParser.SizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(ProtocolParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(ProtocolParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(ProtocolParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(ProtocolParser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#any_except_newline}.
	 * @param ctx the parse tree
	 */
	void enterAny_except_newline(ProtocolParser.Any_except_newlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#any_except_newline}.
	 * @param ctx the parse tree
	 */
	void exitAny_except_newline(ProtocolParser.Any_except_newlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolParser#eol}.
	 * @param ctx the parse tree
	 */
	void enterEol(ProtocolParser.EolContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolParser#eol}.
	 * @param ctx the parse tree
	 */
	void exitEol(ProtocolParser.EolContext ctx);
}