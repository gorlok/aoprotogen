package aoprotogen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.junit.jupiter.api.Test;

import aoprotogen.ProtocolParser.CommandContext;
import aoprotogen.ProtocolParser.CommentContext;
import aoprotogen.ProtocolParser.ParamContext;
import aoprotogen.ProtocolParser.SizeContext;
import aoprotogen.ProtocolParser.TupleContext;
import aoprotogen.ProtocolParser.TypeContext;

public class ProtocolTest {
	
	@Test
	void testName() throws Exception {
		var is = getClass().getResourceAsStream("/protocol.txt");
		CharStream stream = CharStreams.fromStream(is);
		
		var p = new ProtocolParser(
				new CommonTokenStream(new ProtocolLexer(stream)));
		
		p.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
					int charPositionInLine, String msg, RecognitionException e) {
				throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
			}
		});
		

		AtomicReference<List<String>> params = new AtomicReference<>(new ArrayList<String>());
		AtomicReference<String> type = new AtomicReference<>();
		AtomicReference<String> size = new AtomicReference<>();
		AtomicReference<String> tuple = new AtomicReference<>();
		AtomicReference<Boolean> write = new AtomicReference<>(false);
		
		p.addParseListener(new ProtocolBaseListener() {
			@Override
			public void enterType(TypeContext ctx) {
				super.enterType(ctx);
			}
			@Override
			public void exitType(TypeContext ctx) {
				super.exitType(ctx);
				type.set(types(ctx.getChild(0).toString()));
			}
			
			@Override
			public void enterTuple(TupleContext ctx) {
				super.enterTuple(ctx);
			}
			@Override
			public void exitTuple(TupleContext ctx) {
				super.exitTuple(ctx);
				int cant = ctx.getChildCount() - 2;
				String s = "";
				String sep = "";
				while (cant > 0) {
					s = params.get().remove(params.get().size()-1) + sep + s;
					sep = ", ";
					cant--;
				}
				
				tuple.set(s);
			}
			
			@Override
			public void enterSize(SizeContext ctx) {
				super.enterSize(ctx);
				size.set("");
			}
			@Override
			public void exitSize(SizeContext ctx) {
				super.exitSize(ctx);
				String tam = ".".equals(ctx.getText()) ? "" : ctx.getText();
				size.set("[" + tam + "]");
			}
			
			@Override
			public void enterParam(ParamContext ctx) {
				super.enterParam(ctx);
			}
			@Override
			public void exitParam(ParamContext ctx) {
				super.exitParam(ctx);
				String param = ctx.getChild(2).getText();
				String array = size.get() == null ? "" : size.get();
				
				String t;
				if (tuple.get() != null) {
					t = "(" + tuple.get() + ")";
					tuple.set(null);
				} else {
					t = type.get();
				}
				
				params.get().add(t + array + " " + param);
			}
			
			@Override
			public void enterCommand(CommandContext ctx) {
				super.enterCommand(ctx);
				params.get().clear();
			}
			@Override
			public void exitCommand(CommandContext ctx) {
				super.exitCommand(ctx);
				String functionName = capitalize(ctx.getChild(0).toString());
				
				String sep = "";
				StringBuilder sb = new StringBuilder();
				for (int i=0; i<params.get().size(); i++) {
					sb.append(sep)
						.append(params.get().get(i));
					sep = ", ";
				}
				
				if (write.get())
					System.out.format("void write%s(%s);\n", functionName, sb.toString());
				else
					System.out.format("void handle%s(%s);\n", functionName, sb.toString());
				params.get().clear();
				size.set("");
			}
			
			@Override
			public void exitComment(CommentContext ctx) {
				super.exitComment(ctx);
				if (ctx.getText().contains("-------")) {
					// change mode
					write.set(true);
				}
			}
			
		});		
		
		p.parse();
	}
	
	static String capitalize(String s) {
		if (s.length()<2)
			return s.toUpperCase();
		return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
	}

	static String types(String v) {
		switch (v) {
		case "i":
			return "short";
		case "b":
			return "byte";
		case "l":
			return "int";
		case "s":
			return "String";
		case "f":
			return "float";
		}
		return "";
		//return "º"+v+"º";
	}
	
}
