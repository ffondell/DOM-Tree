package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	
	public void build() {
		
		TagNode prev = null;
		TagNode cur = null;
		TagNode curAlt = null;
		Stack<TagNode> tree = new Stack();
		root = new TagNode(sc.next().replace("<", "").replace(">", ""),null,null);
		tree.push(root);
		
		root.firstChild = new TagNode(sc.next().replace("<", "").replace(">", ""),null,null);
		tree.push(root.firstChild);
	
		cur = root.firstChild;
		Boolean alt = false;
		Boolean sib = false;
		String curstr = "";
		Boolean nextAfterNonTag = false;
		String nonTag = "";
		Boolean lastNonTag = false;
		Boolean add = true;
		int slashCount = 0;
		
		while(sc.hasNext()) {
		
			curstr = sc.next();
			if(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
				while(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
					nonTag = nonTag +" "+curstr;
					curstr = sc.next();
					
				}
				if(!alt){
					
					cur.firstChild = new TagNode(nonTag,null,null);
					curAlt = cur.firstChild;
				
					alt = true;
					lastNonTag = true;
					
				}
				else if(alt){
					
					curAlt.firstChild = new TagNode(nonTag,null,null);
					cur = curAlt.firstChild;
					
					alt = false;
					lastNonTag = true;
				}
				nonTag = "";
				
			}	
			
			if(curstr.contains("</")||lastNonTag) {
				
				sib = true;
				
				if(lastNonTag&&!(curstr.contains("</"))) {
					
					if(!alt){
						
						tree.push(cur);
						
						
					}
					else if(alt){
						
						tree.push(curAlt);
						
					}
				}
				if(lastNonTag&&!(curstr.contains("</"))) {
					
					nextAfterNonTag = true;
				}
				lastNonTag = false;
				
			
			}
			
			if(!sib) {
				
				if(!alt){
					
					cur.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					curAlt = cur.firstChild;
					tree.push(curAlt);
				
					alt = true;
				}
				else if(alt){
					
					curAlt.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					cur = curAlt.firstChild;
					tree.push(cur);
					
					alt = false;
				}
			}
			else if(sib||lastNonTag){
				
				if(sc.hasNext()) {
					
					if(!nextAfterNonTag) {
						
						while(curstr.contains("</")&&(sc.hasNext())) {
							
							curstr = sc.next();
							if(slashCount>=1) {
								
								tree.pop();
								
							}
							slashCount++;
						}
						slashCount = 0;
					}
					nextAfterNonTag = false;
					
				}
				if(!alt){
					
					prev = tree.pop();
					if((sc.hasNext())&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
					}
					
					if(sc.hasNext()) {
						prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					}
					curAlt = prev.sibling;
					if(add) {
						tree.push(curAlt);
					}
					add = false;
					
					alt = true;
					sib = false;
					
				}
				else if(alt){
					
					prev = tree.pop();
					
					if((sc.hasNext())&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
					}
					
					if(sc.hasNext()) {
						prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					}
					
					cur = prev.sibling;
					if(add) {
						tree.push(cur);
					}
					
					add = false;
					
					alt = false;
					sib = false;
				}
			}
		}
		
	}
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		
		
		String[] lines = getHTML().split(System.getProperty("line.separator"));
		int i=0;
		root = null;
		TagNode prev = null;
		TagNode cur = null;
		TagNode curAlt = null;
		Stack<TagNode> tree = new Stack();
		
		root = new TagNode(lines[i].replace("<", "").replace(">", ""),null,null);
		tree.push(root);
		
		i++;
		root.firstChild = new TagNode(lines[i].replace("<", "").replace(">", ""),null,null);
		tree.push(root.firstChild);
		i++;
		
		cur = root.firstChild;
		Boolean alt = false;
		Boolean sib = false;
		String curstr = "";
		Boolean nextAfterNonTag = false;
		String nonTag = "";
		Boolean lastNonTag = false;
		Boolean add = true;
		int slashCount = 0;
		
		while(i<lines.length) {
			curstr = lines[i];
			if(curstr.equals("<"+oldTag+">")) {
				curstr = "<"+newTag+">";
			}
		
			if(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
				while(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
					nonTag = nonTag + curstr+" ";
					i++;
					curstr = lines[i];
					if(curstr.equals("<"+oldTag+">")) {
						curstr = "<"+newTag+">";
					}
					
				}
				if(!alt){
					
					cur.firstChild = new TagNode(nonTag,null,null);
					curAlt = cur.firstChild;
					
					alt = true;
					lastNonTag = true;
				}
				else if(alt){
					
					curAlt.firstChild = new TagNode(nonTag,null,null);
					cur = curAlt.firstChild;
					
					alt = false;
					lastNonTag = true;
				}
				nonTag = "";
				
			}	
			
			if(curstr.contains("</")||lastNonTag) {
				
				
				sib = true;
				
				
				if(lastNonTag&&!(curstr.contains("</"))) {
				
					
					if(!alt){
					
						tree.push(cur);
						
						
					}
					else if(alt){
						
						tree.push(curAlt);
						
					}
				}
				if(lastNonTag&&!(curstr.contains("</"))) {
				
					nextAfterNonTag = true;
				}
				lastNonTag = false;
				
				
			}
		
			if(!sib) {
				
				if(!alt){
					
					cur.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					curAlt = cur.firstChild;
					tree.push(curAlt);
					
					alt = true;
				}
				else if(alt){
					
					curAlt.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					cur = curAlt.firstChild;
					tree.push(cur);
					
					alt = false;
				}
			}
			else if(sib||lastNonTag){
				
				if((i<lines.length)) {
					if(!nextAfterNonTag) {
						while(curstr.contains("</")&&(i<lines.length)) {
							
							i++;
							if((i<lines.length)) {
								curstr = lines[i];
								if(curstr.equals("<"+oldTag+">")) {
									curstr = "<"+newTag+">";
								}
							}
							if(slashCount>=1) {
								
								tree.pop();
							
							}
							slashCount++;
						}
						slashCount = 0;
					}
					nextAfterNonTag = false;
					
				}
				if(!alt){
					
					prev = tree.pop();
					if((i<lines.length)&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
					}
					if((i<lines.length)) {
						prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					}
					curAlt = prev.sibling;
					if(add) {
						tree.push(curAlt);
					}
					add = false;
					
					alt = true;
					sib = false;
					
				}
				else if(alt){
					
					prev = tree.pop();
					if((i<lines.length)&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
						
					}
					if((i<lines.length)) {
						prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
						
					}
					cur = prev.sibling;
					if(add) {
						tree.push(cur);
						
					}
					add = false;
					//System.out.println("current top of stack: "+tree.peek());
					alt = false;
					sib = false;
				}
			}
			i++;
		}
		
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		
		String[] lines = getHTML().split(System.getProperty("line.separator"));
		int i=0;
		root = null;
		TagNode prev = null;
		TagNode cur = null;
		TagNode curAlt = null;
		Stack<TagNode> tree = new Stack();
		
		root = new TagNode(lines[i].replace("<", "").replace(">", ""),null,null);
		tree.push(root);
	
		i++;
		root.firstChild = new TagNode(lines[i].replace("<", "").replace(">", ""),null,null);
		tree.push(root.firstChild);
		
		cur = root.firstChild;
		Boolean alt = false;
		Boolean sib = false;
		String curstr = "";
		Boolean nextAfterNonTag = false;
		String nonTag = "";
		Boolean lastNonTag = false;
		Boolean add = true;
		int slashCount = 0;
		Boolean boldrow = false;
		int curow = 0;
		while(i<lines.length) {
			curstr = lines[i];
			
			if(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
				while(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
					nonTag = nonTag + curstr+" ";
					i++;
					curstr = lines[i];
					
					
				}
				if(!alt){
					if(boldrow) {
						
						cur.firstChild = new TagNode("b",null,null);
						curAlt = cur.firstChild;
						curAlt.firstChild = new TagNode(nonTag,null,null);
						cur = curAlt.firstChild;
						lastNonTag = true;
					}
					else {
						
						cur.firstChild = new TagNode(nonTag,null,null);
						curAlt = cur.firstChild;
						
						alt = true;
						lastNonTag = true;
					}
					
				}
				else if(alt){
					if(boldrow) {
						
						curAlt.firstChild = new TagNode("b",null,null);
						cur = curAlt.firstChild;
						cur.firstChild = new TagNode(nonTag,null,null);
						curAlt = cur.firstChild;
						lastNonTag = true;
					}
					else {
						
						curAlt.firstChild = new TagNode(nonTag,null,null);
						cur = curAlt.firstChild;
						
						alt = false;
						lastNonTag = true;
					}
				}
				nonTag = "";
				
			}	
			
			if(curstr.contains("</")||lastNonTag) {
				if(curstr.equals("</tr>")) {
					boldrow = false;
				}
			
				sib = true;
				
				
				if(lastNonTag&&!(curstr.contains("</"))) {
				
					if(!alt){
						
						tree.push(cur);
						
						
					}
					else if(alt){
						
						tree.push(curAlt);
					
					}
				}
				if(lastNonTag&&!(curstr.contains("</"))) {
					
					nextAfterNonTag = true;
				}
				lastNonTag = false;
				
				
			}
			
			if(!sib) {
				
				if(!alt){
					
					cur.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					curAlt = cur.firstChild;
					if((curAlt.tag).equals("tr")) {
						curow++;
						if(curow==row) {
							boldrow = true;
						}
					}
					tree.push(curAlt);
					
					alt = true;
				}
				else if(alt){
					
					curAlt.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					cur = curAlt.firstChild;
					if((cur.tag).equals("tr")) {
						curow++;
						if(curow==row) {
							boldrow = true;
						}
					}
					tree.push(cur);
				
					alt = false;
				}
			}
			else if(sib||lastNonTag){
				
				if((i<lines.length)) {
					if(!nextAfterNonTag) {
						while(curstr.contains("</")&&(i<lines.length)) {
						
							i++;
							if((i<lines.length)) {
								curstr = lines[i];
							}
							if(curstr.equals("</tr>")) {
								boldrow = false;
							}
							if((curstr).equals("<tr>")) {
								curow++;
								if(curow==row) {
									boldrow = true;
								}
							}
							if(slashCount>=1) {
								
								tree.pop();
								
							}
							slashCount++;
						}
						slashCount = 0;
					}
					nextAfterNonTag = false;
				
				}
				if(!alt){
					
					prev = tree.pop();
					if((i<lines.length)&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
					}
					if((i<lines.length)) {
						prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					}
					curAlt = prev.sibling;
					if(add) {
						tree.push(curAlt);
					}
					add = false;
					
					alt = true;
					sib = false;
					
				}
				else if(alt){
					
					prev = tree.pop();
					if((i<lines.length)&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
						
					}
					if((i<lines.length)) {
						prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
						
					}
					cur = prev.sibling;
					if(add) {
						tree.push(cur);
						
					}
					add = false;
					
					alt = false;
					sib = false;
				}
			}
			i++;
		}
		
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		
		String[] lines = getHTML().split(System.getProperty("line.separator"));
		
		for(int k=0;k<lines.length;k++) {
			
			if((lines[k].replaceAll("<", "").replaceAll(">", "")).equals(tag)) {
				
				if((tag.equals("p"))||(tag.equals("em"))||(tag.equals("b"))) {
					lines[k] = lines[k].replaceAll("<"+tag+">", "");
					lines[k] = lines[k].replaceAll("</"+tag+">", "");
					
				}
				else if((tag.equals("ol"))) {
					lines[k] = lines[k].replaceAll("<"+tag+">", "");
					
					for(;k<lines.length;k++) {
						
						if(lines[k].equals("<ul>")) {
							
							for(;k<lines.length;k++) {
								
								if(lines[k].equals("<ol>")) {
									lines[k] = lines[k].replaceAll("<"+tag+">", "");
									for(;k<lines.length;k++) {
										
										
										if(lines[k].equals("<li>")) {
											lines[k] = lines[k].replaceAll("<li>", "<p>");
											
										}
										if(lines[k].equals("</li>")) {
											
											lines[k] = lines[k].replaceAll("</li>", "</p>");
										}
										if(lines[k].equals("</"+tag+">")) {
											lines[k] = lines[k].replaceAll("</"+tag+">", "");
										
											break;
										}
									}
								}
								if(lines[k].equals("</ul>")) {
									
									break;
								}
							}
						}
						if(lines[k].equals("<li>")) {
							lines[k] = lines[k].replaceAll("<li>", "<p>");
							
						}
						if(lines[k].equals("</li>")) {
						
							lines[k] = lines[k].replaceAll("</li>", "</p>");
						}
						if(lines[k].equals("</"+tag+">")) {
							lines[k] = lines[k].replaceAll("</"+tag+">", "");
							
							break;
						}
					}
				}
				else if((tag.equals("ul"))) {
					lines[k] = lines[k].replaceAll("<"+tag+">", "");
					
					for(int j=k;j<lines.length;j++) {
						
						if(lines[j].equals("<ol>")) {
							for(;j<lines.length;j++) {
								if(lines[j].equals("</ol>")) {
									break;
								}
							}
						}
						if(lines[j].equals("<li>")) {
							lines[j] = lines[j].replaceAll("<li>", "<p>");
							
						}
						if(lines[j].equals("</li>")) {
							
							lines[j] = lines[j].replaceAll("</li>", "</p>");
						}
						if(lines[j].equals("</"+tag+">")) {
							lines[j] = lines[j].replaceAll("</"+tag+">", "");
							
							break;
						}
					}
				}
			}
		}
		for(int u=0;u<lines.length;u++) {
			System.out.println(lines[u]);
		}
		
		int i=0;
		root = null;
		TagNode prev = null;
		TagNode cur = null;
		TagNode curAlt = null;
		Stack<TagNode> tree = new Stack();
		
		root = new TagNode(lines[i].replace("<", "").replace(">", ""),null,null);
		tree.push(root);
	
		i++;
		root.firstChild = new TagNode(lines[i].replace("<", "").replace(">", ""),null,null);
		tree.push(root.firstChild);
		
		i++;
		cur = root.firstChild;
		Boolean alt = false;
		Boolean sib = false;
		String curstr = "";
		Boolean nextAfterNonTag = false;
		String nonTag = "";
		Boolean lastNonTag = false;
		Boolean add = true;
		int slashCount = 0;
		
		while(i<lines.length) {
			curstr = lines[i];
			if(curstr.equals("")) {
				if(i<lines.length) {
					i++;
					curstr = lines[i];
				}
			}
			
			if(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
				while(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
					nonTag = nonTag + curstr+" ";
					i++;
					curstr = lines[i];
					if(curstr.equals("")) {
						if(i<lines.length) {
							i++;
							curstr = lines[i];
						}
					}
					
				
				}
				if(!alt){
					
					cur.firstChild = new TagNode(nonTag,null,null);
					curAlt = cur.firstChild;
					
					alt = true;
					lastNonTag = true;
				}
				else if(alt){
					
					curAlt.firstChild = new TagNode(nonTag,null,null);
					cur = curAlt.firstChild;
					
					alt = false;
					lastNonTag = true;
				}
				nonTag = "";
				
			}	
			
			if(curstr.contains("</")||lastNonTag) {
				
				
				sib = true;
				
			
				if(lastNonTag&&!(curstr.contains("</"))) {
					
					if(!alt){

						tree.push(cur);
						
						
					}
					else if(alt){
						
						tree.push(curAlt);
						
		
					}
				}
				if(lastNonTag&&!(curstr.contains("</"))) {
					
					nextAfterNonTag = true;
				}
				lastNonTag = false;
				
			}
		
			if(!sib) {
			
				if(!alt){
					
					cur.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					curAlt = cur.firstChild;
					tree.push(curAlt);
					
					alt = true;
				}
				else if(alt){
					
					
					curAlt.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					cur = curAlt.firstChild;
					tree.push(cur);
					
					alt = false;
				}
			}
			else if(sib||lastNonTag){
			
				if((i<lines.length)) {
					if(!nextAfterNonTag) {
						while(curstr.contains("</")&&(i<lines.length)) {
							
							i++;
							if((i<lines.length)) {
								curstr = lines[i];
								if(curstr.equals("")) {
									if(i<lines.length) {
										i++;
										curstr = lines[i];
									}
								}
							}
							if(slashCount>=1) {
								
								tree.pop();
								
							}
							slashCount++;
						}
						slashCount = 0;
					}
					nextAfterNonTag = false;
					
				}
				if(!alt){
					
					
					prev = tree.pop();
					if((i<lines.length)&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
					}
					if((i<lines.length)) {
						prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					}
					curAlt = prev.sibling;
					if(add) {
						tree.push(curAlt);
					}
					add = false;
					//System.out.println("current top of stack: "+tree.peek());
					alt = true;
					sib = false;
					
				}
				else if(alt){
					
					prev = tree.pop();
					if((i<lines.length)&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
						
					}
					if((i<lines.length)) {
						prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
						
					}
					cur = prev.sibling;
					if(add) {
						tree.push(cur);
						
					}
					add = false;
					//System.out.println("current top of stack: "+tree.peek());
					alt = false;
					sib = false;
				}
			}
			i++;
		}
		
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	private static String[] addPos(String[] a, int pos, String rep) {
	    String[] result = new String[a.length];
	    for(int i = 0; i < pos; i++) {
	        result[i] = a[i];
	    }
	    result[pos] = rep;
	    for(int i = pos + 1; i < a.length; i++) {
	    		result[i] = a[i - 1];
	    }
	        
	    return result;
	}
	private static String[] removeElement(String[] arr, int removedIdx) {
	    System.arraycopy(arr, removedIdx + 1, arr, removedIdx, arr.length - 1 - removedIdx);
	    return arr;
	}
	public void addTag(String word, String tag) {

		String[] lines = getHTML().split(System.getProperty("line.separator"));
		String a = "";
		String curstring = "";
		String split = "";
		
		for(int h=0;h<lines.length;h++) {
			
			if(!(lines[h].contains("<"))) {
				
				for(int e=0;e<lines[h].length();e++){
			
					if((lines[h].substring(e,e+1)).equals(" ")) {
						
						if((curstring.equalsIgnoreCase(word))||(curstring.equalsIgnoreCase(word+"!"))||(curstring.equalsIgnoreCase(word+"?"))||(curstring.equalsIgnoreCase(word+"."))||(curstring.equalsIgnoreCase(word+";"))||(curstring.equalsIgnoreCase(word+":"))) {
						
						
							lines = addPos(lines,h,"<"+tag+">");
							h++;
							lines = addPos(lines,h,curstring);
							h++;
							lines = addPos(lines,h,"</"+tag+">");
							h++;
							curstring = "";
						}
						else {
							
							lines = addPos(lines,h,curstring);
							h++;
							
							if(e+1==lines[h].length()) {
								lines = removeElement(lines,h);
							}
							curstring = "";
							
						}
					}
					else {
						
						curstring = curstring + lines[h].substring(e,e+1);

					}
						
				}
				curstring = "";
				split = "";
			}
			
		}
		
	
		
		
		
		int i=0;
		root = null;
		TagNode prev = null;
		TagNode cur = null;
		TagNode curAlt = null;
		Stack<TagNode> tree = new Stack();
		
		root = new TagNode(lines[i].replace("<", "").replace(">", ""),null,null);
		tree.push(root);
		
		i++;
		root.firstChild = new TagNode(lines[i].replace("<", "").replace(">", ""),null,null);
		tree.push(root.firstChild);
		i++;
		
		cur = root.firstChild;
		Boolean alt = false;
		Boolean sib = false;
		String curstr = "";
		Boolean nextAfterNonTag = false;
		String nonTag = "";
		Boolean lastNonTag = false;
		Boolean add = true;
		int slashCount = 0;
		Boolean lastTarTag = false;
		while(i<lines.length) {
			curstr = lines[i];
			
			if(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
				while(!((curstr.contains("html>"))||(curstr.contains("body>"))||(curstr.contains("p>"))||(curstr.contains("html>"))||(curstr.contains("em>"))||(curstr.contains("b>"))||(curstr.contains("table>"))||(curstr.contains("tr>"))||(curstr.contains("td>"))||(curstr.contains("ol>"))||(curstr.contains("ul>"))||(curstr.contains("li>")))) {
					nonTag = nonTag + curstr+" ";
					i++;
					curstr = lines[i];
					
					
				}
				if(!alt){
					
					cur.firstChild = new TagNode(nonTag,null,null);
					curAlt = cur.firstChild;
					
					alt = true;
					lastNonTag = true;
				}
				else if(alt){
					
					curAlt.firstChild = new TagNode(nonTag,null,null);
					cur = curAlt.firstChild;
					
					alt = false;
					lastNonTag = true;
				}
				nonTag = "";
				
			}	
		
			if(curstr.contains("</")||lastNonTag) {
				if(curstr.equals("</"+tag+">")) {
					lastTarTag = false;
				}
				
				sib = true;
				
			
				if(lastNonTag&&!(curstr.contains("</"))) {
				
					if(!alt){
						
						tree.push(cur);
						
						
					}
					else if(alt){
						
						tree.push(curAlt);
						
					}
				}
				if(lastNonTag&&!(curstr.contains("</"))) {
					
					nextAfterNonTag = true;
				}
				lastNonTag = false;
				
				
			}
			
			if(!sib) {
			
				if(!alt){
				
					cur.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					curAlt = cur.firstChild;
					tree.push(curAlt);
					
					alt = true;
				}
				else if(alt){
					
					
					curAlt.firstChild = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					cur = curAlt.firstChild;
					tree.push(cur);
					
					alt = false;
				}
			}
			else if(sib||lastNonTag){
				
				if((i<lines.length)) {
					if(!nextAfterNonTag) {
						while(curstr.contains("</")&&(i<lines.length)) {
							
							i++;
							
							if((i<lines.length)) {
								curstr = lines[i];
								
							}
							if(!(curstr.contains("<"))) {
								int e = i;
								e++;
								
								while(!(lines[e].contains("<"))) {
									
									i++;
									curstr = curstr +" "+ lines[i];
									e++;
								}

							}
							if(slashCount>=1) {
								
								tree.pop();
								
							}
							slashCount++;
						}
						slashCount = 0;
					}
					nextAfterNonTag = false;
					
				}
				if(!alt){
					
					prev = tree.pop();
					if((i<lines.length)&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
					}
					if((i<lines.length)) {
							prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
						
					}
					curAlt = prev.sibling;
					if(add) {
						tree.push(curAlt);
					}
					add = false;
					
					alt = true;
					sib = false;
					
				}
				else if(alt){
					
					prev = tree.pop();
					if((i<lines.length)&&(((curstr).contains("html>"))||((curstr).contains("body>"))||((curstr).contains("p>"))||((curstr).contains("html>"))||((curstr).contains("em>"))||((curstr).contains("b>"))||((curstr).contains("table>"))||((curstr).contains("tr>"))||((curstr).contains("td>"))||((curstr).contains("ol>"))||((curstr).contains("ul>"))||((curstr).contains("li>")))) {
						add = true;
						
					}
					if((i<lines.length)) {
						prev.sibling = new TagNode(curstr.replace("<", "").replace(">", ""),null,null);
					
					}
					cur = prev.sibling;
					if(add) {
						tree.push(cur);
						
					}
					add = false;
					//System.out.println("current top of stack: "+tree.peek());
					alt = false;
					sib = false;
				}
			}
			i++;
		}
		
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
