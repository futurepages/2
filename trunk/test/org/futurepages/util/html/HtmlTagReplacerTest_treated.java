package org.futurepages.util.html;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HtmlTagReplacerTest_treated {

	private HtmlTagReplacer replacer;
	private String tag;
	private String expected;
	private String caso;
	
	public HtmlTagReplacerTest_treated(HtmlTagReplacer replacer, String tag,
			String expected, String caso) {
		super();
		this.replacer = replacer;
		this.tag = tag;
		this.expected = expected;
		this.caso = caso;
	}
	
	@Test
	public void testTreated() {
		String result = replacer.treated(this.tag);
		Assert.assertEquals(caso, expected, result);
	}

	private static HtmlTagReplacer styles(){
		return new HtmlTagReplacer(true, false, false, false, false);
	}
	
	private static HtmlTagReplacer noStyles(){
		return new HtmlTagReplacer(false, false, false, false, false);
	}
	
	private static HtmlTagReplacer lists(boolean style){
		return new HtmlTagReplacer(style, true, false, false, false);
	}
	
	private static HtmlTagReplacer noLists(){
		return new HtmlTagReplacer(false, false, false, false, false);
	}

	private static HtmlTagReplacer image(boolean style){
		return new HtmlTagReplacer(style, false, true, false, false);
	}
	private static HtmlTagReplacer noImage(boolean style){
		return new HtmlTagReplacer(style, false, false, false, false);
	}
	
	private static HtmlTagReplacer anchor(boolean style){
		return new HtmlTagReplacer(style, false, false, true, false );
	}
	
	private static HtmlTagReplacer noAnchor(boolean style){
		return new HtmlTagReplacer(style, false, false, false, false );
	}
	
	private static HtmlTagReplacer noTable(boolean style){
		return new HtmlTagReplacer(style, false, false, false, false);
	}
	
	private static HtmlTagReplacer table(boolean style){
		return new HtmlTagReplacer(style, false, false, false, true);
	}
	
	@Parameters
	public static Collection parameters() {
		Collection col =  Arrays.asList(new Object[][] {
			
			{noStyles(),	"<a/>", 		"<span style=\"text-decoration:underline;\"/>", 	"tag <a> com style."},
			
			{styles(), 		"<a/>", 		"<span style=\"text-decoration:underline;\"/>", 	"tag <a> com style."},
			{styles(), 		"<td>", 		"&nbsp; - ",	 									"tag <td> sem tables com style."},

			{styles(), 		"<p/>", 		"<p/>", 											"tag <p> com style."},
			{styles(), 		"<strong/>", 	"<strong/>", 										"tag <strong> com style."},
			{styles(), 		"<u/>", 		"<span style=\"text-decoration:underline;\"/>",		"tag <u/>."},
			{styles(), 		"<adress/>", 	"", 												"tag <adress> "},
			{styles(), 		"<b/>",			 			"<strong/>",							"tag <b> com style."},
			{styles(), 		"<big/>", 					"<strong/>",							"tag <big> com style."},
			{styles(), 		"<h1/>",	 				"<h1/>", 								"tag <h1> com style."},
			{styles(), 		"<h2/>",			 		"<h2/>", 								"tag <h2> com style."},
			{styles(), 		"<h3/>", 					"<h3/>", 								"tag <h3> com style."},
			{styles(), 		"<h4/>", 					"<h4/>", 								"tag <h4> sem style."},
			{noStyles(), 	"<h1 alt=\"1\"/>", 			"<p/>", 								"tag <h1> sem style."},
			{noStyles(), 	"<h2 alt=\"1\"/>", 			"<p/>", 								"tag <h2> sem style."},
			{noStyles(), 	"<h3 alt=\"1\"/>", 			"<p/>",	 								"tag <h3> sem style."},
			{noStyles(), 	"<h4 alt=\"1\"/>", 			"<p/>", 								"tag <h4> sem style."},
			
			{lists(true), 	"<ul alt=\"1\"/>", 			"<ul alt=\"1\"/>", 						"tag <ul> com style."},
			{lists(true), 	"<ol alt=\"1\"/>", 			"<ol alt=\"1\"/>", 						"tag <ol> com style."},
			{lists(true), 	"<li alt=\"1\"/>",	 		"<li alt=\"1\"/>", 						"tag <li> com style."},
			{lists(true), 	"<blockote/>", 				"<blockote/>",							"tag <blockote/> com style."},
			{lists(true), 	"<ul alt=\"1\"/>", 			"<ul alt=\"1\"/>", 						"tag <ul> com style."},
			{lists(false), 	"<ul alt=\"1\"/>",	 		"<ul/>", 								"tag <ul> sem style."},
			{lists(false), 	"<ol alt=\"1\"/>", 			"<ol/>", 								"tag <ol> sem style."},
			{lists(false), 	"<li alt=\"1\"/>",	 		"<li/>", 								"tag <li> sem style."},
			{lists(false), 	"<blockote  alt=\"1\"/>", 	"<blockote/>", 							"tag <blockote/> sem style."},
			{lists(false), 	"<ul alt=\"1\"/>", 			"<ul/>", 								"tag <ul> sem style."},
			
			{noLists(), 	"<ul/>", 					"<p/>", 								"tag <ul> sem lists."},
			{noLists(), 	"<ol/>", 					"<p/>", 								"tag <ol> sem lists."},
			{noLists(), 	"<li/>", 					"&nbsp; - ", 							"tag <li> sem lists."},
			
			{image(true), 	"<img src=\"a\" alt=\"d\" alt2=\"d\">","<img src=\"a\" alt=\"d\" alt2=\"d\">",	"tag <img> com imagens com style."},
			{image(false), 	"<img src=\"a\" alt=\"d\" alt2=\"d\">","<img src=\"a\" alt=\"d\">",				"tag <img> com imagens sem style."},
			{noImage(true), "<img src=\"a\" alt=\"d\" alt2=\"d\">","",										"tag <img> sem imagens com style."},
			{noImage(false),"<img src=\"a\" alt=\"d\" alt2=\"d\">","",										"tag <img> sem imagens sem style."},
			
			{anchor(true),		"<a href=\"w\" style=\"a\" />", 			"<a href=\"w\" style=\"a\" />",					"tag <a> com links com style."},
			{anchor(false),		"<a href=\"w\" target=\"tg\" alt=\"al\"/>",	"<a href=\"w\" target=\"tg\"/>",	 			"tag <a> com links sem style."},
			{noAnchor(true),	"<a href=\"w\"/>", 							"<span style=\"text-decoration:underline;\"/>",	"tag <a> sem links com style."},
			{noAnchor(false),	"<a href=\"w\" target=\"tg\"/>",			"<span style=\"text-decoration:underline;\"/>", "tag <a> sem links sem style."},
			
			{table(true), 	"<td>", 		"<td>", 											"tag <td> com tables com style."},
			{table(false), 	"<td alt=\"2\" colspan=\"3\">", 		"<td colspan=\"3\">", 		"tag <td> com tables com style."},
			{noTable(false),"<td colspan=\"3\">", 					"&nbsp; - ", 				"tag <td> sem tables sem style."},
			{noTable(true), "<td colspan=\"3\">", 					"&nbsp; - ", 				"tag <td/> sem tables com style."},

			{table(true), 	"<table alt=\"1\">", 	"<table alt=\"1\">", 	"tag <table> com tables com style."},
			{table(false), 	"<table alt=\"1\">",	"<table>", 				"tag <table> com tables sem style."},
			{noTable(true), "<table>",				"", 					"tag <table> sem tables."},
			
			{table(true), 	"<caption>", 			"<caption>",			"tag <caption> com tables."},
			{noTable(true), "<caption>",			"", 					"tag <caption> sem tables."},
			{table(true), 	"<tbody style=\"a\">",	"<tbody style=\"a\">", 	"tag <tbody> com tables."},
			{noTable(true), "<tbody>",				"", 					"tag <tbody> sem tables."},
			
			{table(true), 	"<thead style=\"a\">",	"<thead style=\"a\">",	"tag <thead> com tables com style."},
			{table(false), 	"<thead style=\"a\">",	"<thead>",				"tag <thead> com tables sem style."},
			{noTable(true), "<thead>",				"<p>", 					"tag <thead> sem tables."},
			
			{table(true), 	"<tfoot style=\"a\">", 	"<tfoot style=\"a\">", 	"tag <tfoot> com tables."},
			{noTable(true), "<tfoot>",				"<p>", 					"tag <tfoot> sem tables."},
			{table(true), 	"<tr roswpan=\"qwer\" alt=\"1\">", 	"<tr roswpan=\"qwer\" alt=\"1\">", "tag <tr> com tables com style."},
			{table(false), 	"<tr roswpan=\"qwer\" alt=\"1\">", 	"<tr>", 	"tag <tr> com tables sem style."},
			{noTable(true), "<tr roswpan=\"q\">", 		"<p roswpan=\"q\">","tag <tr> sem tables com style."},
			{noTable(false),"<tr roswpan=\"qwer\">",	"<p>", 				"tag <tr> sem tables sem style."},
			{noTable(false),"<th>",	 					"<p>", 				"tag <th> sem tables."},
			

		});
		return col;
	}

}