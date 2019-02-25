package aoprotogen;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import aoprotogen.ProtocolParser.CommandContext;
import aoprotogen.ProtocolParser.CommentContext;

public class DecoderTest {
	
	@Test
	void testName() throws Exception {
		try {
			Path file = Paths.get("out/");
			Files.createDirectory(file);
		} catch (FileAlreadyExistsException ignore) {}
		
		var is = getClass().getResourceAsStream("/protocol.txt");
		CharStream stream = CharStreams.fromStream(is);
		
		var parser = new ProtocolParser(
				new CommonTokenStream(new ProtocolLexer(stream)));
		
		ParseTree tree = parser.parse();
		
		var visitor = new DecoderGenVisitor();
		visitor.visit(tree);
	}
	
}

class DecoderGenVisitor extends ProtocolBaseVisitor<Protocol> {
	
	public static final String OUT = "./out/";
	boolean clientMode = false;
	
	@Override
	public Protocol visit(ParseTree tree) {
		var r = super.visit(tree);
		return r;
	}
	
	@Override
	public Protocol visitComment(CommentContext ctx) {
		if (ctx.getText().contains("-------")) {
			this.clientMode = true;
			var r = super.visitComment(ctx);
			return r;
		}
		return super.visitComment(ctx);
	}

	String command="";
	@Override
	public Protocol visitCommand(CommandContext ctx) {
		this.command = ctx.getChild(0).toString();
		String baseClazz = (clientMode ? "ServerPacket" : "ClientPacket");
		String sufix = (clientMode ? "Response" : "Request");
		String className = ctx.getChild(0) + sufix;
		
		ctx.params().forEach(p -> visitParams(p));
		
		if (!clientMode) {
			System.out.println("case " + ctx.getChild(0) + ":");
			System.out.println("\tpacket = "+ className +".decode(in);");
			System.out.println("\tbreak;");
			
			//writeFile(className + ".java", text);
		}
		
		return super.visitCommand(ctx);
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
	}
	
	private static void writeFile(String fileName, String text) {
		Path file = Paths.get(OUT + fileName);
		try {
			Files.writeString(file, text, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
