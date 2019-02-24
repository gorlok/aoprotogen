package aoprotogen;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import aoprotogen.ProtocolParser.CommandContext;
import aoprotogen.ProtocolParser.CommentContext;
import aoprotogen.ProtocolParser.ParamContext;
import aoprotogen.ProtocolParser.ParameterContext;
import aoprotogen.ProtocolParser.SizeContext;
import aoprotogen.ProtocolParser.TupleContext;
import aoprotogen.ProtocolParser.TypeContext;

public class ProtocolTest {
	
	@Test
	void testName() throws Exception {
		var is = getClass().getResourceAsStream("/protocol.txt");
		CharStream stream = CharStreams.fromStream(is);
		
		var parser = new ProtocolParser(
				new CommonTokenStream(new ProtocolLexer(stream)));
		
		ParseTree tree = parser.parse();
		
		var visitor = new MyProtocolVisitor();
		visitor.visit(tree);
	}
	
}

class MyProtocolVisitor extends ProtocolBaseVisitor<Protocol> {
	
	boolean clientMode = false;
	
	List<Param> params = new ArrayList<>();
	
	@Override
	public Protocol visit(ParseTree tree) {
		System.out.println("// SERVER COMMANDS ---------------");
		System.out.println("abstract class ServerPacket {};\n");
		var r = super.visit(tree);
		System.out.println("// FIN");
		return r;
	}
	
	@Override
	public Protocol visitComment(CommentContext ctx) {
		if (ctx.getText().contains("-------")) {
			this.clientMode = true;
			System.out.println("\n// CLIENT COMMANDS -----------------");
			System.out.println("abstract class ClientPacket {};\n");			
			var r = super.visitComment(ctx);
			return r;
		}
		return super.visitComment(ctx);
	}

	String command="";
	@Override
	public Protocol visitCommand(CommandContext ctx) {
		this.command = ctx.getChild(0).toString();
		System.out.println("// " + ctx.getText());
		String baseClazz = (clientMode ? "ClientPacket" : "ServerPacket");
		String sufix = (clientMode ? "Response" : "Request");
		String className = ctx.getChild(0) + sufix;
		
		params.clear();
		
		ctx.params().forEach(p -> visitParams(p));
		
		var fieldList = createFieldList(this.params);
		
		var constructor = createConstructor(className, this.params);
		
		System.out.format(
				"class %s extends %s {\n" +
				"%s"+
				"%s"+
				"};\n\n",
				className, baseClazz, fieldList, constructor);
		
		return super.visitCommand(ctx);
	}

	private String createFieldList(List<Param> list) {
		var fieldList = list.stream()
				.map(p -> "\tpublic " + p + ";\n")
				.reduce("", String::concat);
		return fieldList;
	}
	
	private String createConstructor(String className, List<Param> list) {
		var fieldList = list.stream()
				.map(p -> p + ";")
				.reduce("", String::concat);
		
		if (fieldList.length()>0 && fieldList.charAt(fieldList.length()-1) == ';') {
			fieldList = fieldList.substring(0, fieldList.length()-1);
		}

		var assigns = list.stream()
				.map(p -> "\t\tthis." + p.name + " = " + p.name + ";\n")
				.reduce("", String::concat);
		
		return String.format("\tpublic %s(%s){\n%s\t}\n", className, fieldList, assigns);
	}
	
	@Override
	public Protocol visitParameter(ParameterContext ctx) {
		array = "";
		params.add(new Param(ctx.getText(), type));
		return super.visitParameter(ctx);
	}
	
	@Override
	public Protocol visitParam(ParamContext ctx) {
		return super.visitParam(ctx);
	}
	
	String type;
	@Override
	public Protocol visitType(TypeContext ctx) {
		var r = super.visitType(ctx);
		if (ctx.getChildCount() == 1) {
			type = types(ctx.getText());
		} else {
			// ARRAY
			visitSize(ctx.size());
			if (!"".equals(tupla)) {
				type = tupla;
				tupla = "";
			}
			type = type + "[]";
		}
		return r;
	}

	String array="";
	@Override
	public Protocol visitSize(SizeContext ctx) {
		// array
		this.array = ctx.getText();
		return super.visitSize(ctx);
	}

	String tupla = "";
	@Override
	public Protocol visitTuple(TupleContext ctx) {
		this.tupla = "";
		var index = this.params.size();
		var r = super.visitTuple(ctx);
		
		var itemsTupla = this.params.subList(index, this.params.size());
		
		// generar TUPLA
		this.tupla = this.command + "_DATA";
		
		var fieldList = createFieldList(itemsTupla);
		
		var constructor = createConstructor(this.tupla, itemsTupla);
		
		System.out.format("class %s_DATA {\n%s%s};\n", this.command, fieldList, constructor);

		// cambiar params por tupla
		params = this.params.subList(0, index);
		
		
		return r;
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
}



class Protocol {
}

class Param {
	String name;
	String type;
	public Param(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}
	@Override
	public String toString() {
		return this.type + " " + name;
	}
}
