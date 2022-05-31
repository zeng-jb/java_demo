FIRST集求法：

 

我 对First集的理解：first集应该就是求一个表示文法的字串（一般指非终结符，终结符的first集就是它自身）开头的所有可能出现的字符的集合。 例如A->aC | bB | cD,根据这个产生式，就可以知道，非终结符A，被替换后，它开头可能出现字符有a、b 、c, 所以 {a,b,c}是First（A）的一个子集。

 

求First集的步骤：

若X->a..，则将终结符ａ加入FIRST(X)中;（注意非终结符的情况）

若X->e ,则将终结符e加入FIRST(X)中(e表示空集)；

若 X->BC..D,则将First（B）所有元素（除了空集）加入First（A），然后检测First（B），若First（B）中不存在空集, 即e,则停止，若存在则向B的后面查看，将First（C）中所有元素（除了空集）加入First（A），然后再检测First（C）中是否有e...直 到最后，若D之前的所有非终结符的First集中都含有e,则检测到D时，将First（D）也加入First（A），若First（D）中含有e,则将 e加入First（A）。

对于第三条，其实也很好理解，就是说当X推导出一个字串时，D前面的非终结符都可能推出空串，这个时候，X推出的串的首部，就不是那些推出空串的非终结符了，而是这些推出空串的非终结符后面的文法符号所推导出的字串。

                例题：设文法G（S）：

                           S->S+aF|aF|+aF

                           F->*aF|*a

                       (1)消除左递归和左因子。

                       (2)构造相应的FIRST集合和FOLLOW集合。

  解析：首先，第一个式子，消除左递归。S->aFS’|+aFS'。S'->+aFS'|ε             （此处的ε和e一样的 不同的书上印刷的不同）

              然后，第二个式子，消除左因子。F->*aF’，F'->F|ε

             第三步，求各个非终结符的FIRST集合。FIRST（S）={a，+}     因为S可以导出首字母为终结符a的产生式，和首字符为+的产生式。

                                                                                    FIRST（S‘）={+，ε}  FIRST（F）={*}，FIRST（F’）={*，ε}

FOLLOW集的求法：

 

对Follow集，其实也差不多，它应该是指非终结符推出的字串最末端后可能出现的所有字符的集合。例如Follow(U)所表达的是句型中非终结符U所有可能的后随终结符号的集合，特别地，“$”是识别符号的后随符。注意Follow集合是从开始符号S开始推导。

 

求Follow集的步骤：

对文法开始符号S,置$于FOLLOW(S)中；      （也就是说有关S的FOLLOW集合中，都包含$，也有书中表示为#）

对于产生式：A->aBC,将除去空集e的First（C）加入Follow（B）中;      （B后面跟着的就是C的首部字符，注意如果是终结符也要搞上）

对于产生式：A->aB或者A->aBC,(其中C可以推导出空串，C=>*e，即空串属于C的first集合),则将Follow（A）加入Follow（B）中。                                                   （注意：此处a可以是空，也可以是其他文法符号）；

                      （A->aB ,那么A推出字串的末端后字符集合，与B推出字串的末端后字符集合，是等价的。）

注意：follow集合是要将所有的产生式都找出来，来求非终结符的follow集合。

             刚刚的例题：

                     FOLLOW（S）={#}，FOLLOW（S'）={#}，可以看出S和S‘ 并没有其他的末端。

                     FOLLOW（F）={+，#}    （原因是S->aFS’|+aFS'。S'->+aFS'|ε  ，由第二条，讲S和S’的FOLLOW集合加入到FOLLOW（F）中，且F后面有S‘，因此将                                                                                           FIRST（S’）出去空集以外，也加入到FOLLOW（F）中 ）

                     FOLLOW（F'）={+，#}，（F->*aF’，F'->F|ε，互相加入到集合中去）
                     
                     
                     
  
  
/*
		消除左递归
	*/
	
	void remove_left_recursion(){
		string tempVn = "";
		for (auto it = Vn.begin(); it != Vn.end(); it++) {
			tempVn += *it;
		}
		
		for (int i = 0; i < tempVn.length(); i++) {
			char pi = tempVn[i];
			set<string>NewPRight;
			for (auto it = P[pi].begin(); it != P[pi].end(); it++) {
				bool isget = 0;
				string right = *it;
				for (int j = 0; j < i; j++) {
					char pj = tempVn[j];
					if (pj == right[0]) {
						isget = 1;
						for (auto it1 = P[pj].begin(); it1 != P[pj].end(); it1++) {
							string s = *it1 + right.substr(1);
							NewPRight.insert(s);
						}
					}
				}
				if (isget == 0) {
					NewPRight.insert(right);
				}
			}
			/*
			for (int j = 0; j < i; j++) {
				char pj=tempVn[j];
				for (auto it = P[pi].begin(); it != P[pi].end(); it++) {
					string right = *it;
					if (right[0] == pj) {
						for (auto itpj = P[pj].begin(); itpj != P[pj].end(); itpj++) {
							string s = *itpj + right.substr(1);
							NewPRight.insert(s);
						}
					}
					else {
						NewPRight.insert(right);
					}
				}
			}
			*/
			if(i!=0)
				P[pi] = NewPRight;

			remove_left_gene(pi);
		}
	}
	/*
		提取左因子
	*/
	void remove_left_gene(char c) {
		char NewVn;
		for (int i = 0; i < 26; i++) {
			NewVn = i + 'A';
			if (!Vn.count(NewVn)) {
				break;
			}
		}
		bool isaddNewVn = 0;
		for (auto it = P[c].begin(); it != P[c].end(); it++) {
			string right = *it;
			
			if (right[0] == c) {
				isaddNewVn = 1;
				
				break;
			}
		}
		if (isaddNewVn) {
			set<string>NewPRight;
			set<string>NewPNewVn;
			for (auto it = P[c].begin(); it != P[c].end(); it++) {
				string right = *it;
				if (right[0] != c) {
					right += NewVn;
					NewPRight.insert(right);
				}
				else {
					right = right.substr(1);
					right += NewVn;
					NewPNewVn.insert(right);
				}
			}
			Vn.insert(NewVn);
			NewPNewVn.insert("@");
			P[NewVn] = NewPNewVn;
			P[c] = NewPRight;
		}
	}
             
             
             
 



    public void removeLeftRecursion() {
		for (int i = 0; i < symbols.size(); i++) {
			for (int j = 0; j < i; j++) {
				iterativeReplacement(symbols.get(i), symbols.get(j));
			}
			removeLeftRecursion(symbols.get(i));
		}
		no_or_is_terminatingSymbol();
	}
 
 
	private void no_or_is_terminatingSymbol() {
		for (int i = 0; i < productions.size(); i++) {
			if (!((ArrayList) nonTerminatingSymbol)
					.contains(productions.get(i).getLeft())) {
				nonTerminatingSymbol.add(productions.get(i).getLeft());
			}
			if (productions.get(i).getLeft() == productions.get(i).getLeft()
					.charAt(0)
					+ "'") {
				nonTerminatingSymbol.add(productions.get(i).getLeft());
			}
		}
		for (int i = 0; i < productions.size(); i++) {
			String temp = productions.get(i).getRight();
			temp = temp.replace("epsilon", "#");
			for (int j = 0; j < nonTerminatingSymbol.size(); j++) {
				temp = temp.replaceAll(nonTerminatingSymbol.get(j), "");
			}
			temp = temp.replaceAll("\\\\|", "");
			temp = temp.replaceAll("'", "");
			char[] chars = temp.toCharArray();
			for (int k = 0; k < chars.length; k++) {
				if (chars[k] == '#') {
					if (!terminatingSymbol.contains("epsilon")) {
						terminatingSymbol.add("epsilon");
					}
				} else {
					if (!terminatingSymbol.contains(String.valueOf(chars[k]))) {
						terminatingSymbol.add(String.valueOf(chars[k]));
					}
				}
			}
		}
	}
 
	private void iterativeReplacement(Character left, Character right) {
		ListIterator listIterator = productions.listIterator();
		while (listIterator.hasNext()) {
			String inRight = "";
			Grammar grammar = listIterator.next();
			if (grammar.getLeft().equals(left.toString())) {
				boolean isReplacement = false;
				String[] rights = grammar.getRight().split("\\\\|");
				for (int i = 0; i < rights.length; i++) {
					if (rights[i].startsWith(right.toString())) {
						isReplacement = true;
					}
				}
				if (isReplacement) {
					ListIterator _listIterator = productions
							.listIterator();
					while (_listIterator.hasNext()) {
						Grammar _grammar = _listIterator.next();
						if (_grammar.getLeft().equals(right.toString())) {
							String[] _rights = _grammar.getRight().split("\\\\|");
							for (int i = 0; i < rights.length; i++) {
								boolean isCheck = false;
								if (rights[i].startsWith(right.toString())) {
									isCheck = true;
									for (int j = 0; j < _rights.length; j++) {
										String temp = rights[i];
										inRight += (temp.replaceFirst(
												right.toString(), _rights[j]) + "|");
									}
								}
								if (!isCheck) {
									inRight += (rights[i] + "|");
								}
							}
						}
					}
					if (inRight.length() != 0) {
						listIterator.remove();
						listIterator.add(new Grammar(left.toString(), inRight
								.substring(0, inRight.length() - 1)));
					}
				}
			}
		}
	}
 
	private void removeLeftRecursion(Character left) {
		ListIterator listIterator = productions.listIterator();
		while (listIterator.hasNext()) {
			Grammar grammar = listIterator.next();
			if (grammar.getLeft().equals(left.toString())) {
				String[] rights = grammar.getRight().split("\\\\|");
				boolean isLeftRecursion = false;
				for (int i = 0; i < rights.length; i++) {
					if (rights[i].startsWith(left.toString())) {
						isLeftRecursion = true;
					}
				}
				if (isLeftRecursion) {
					listIterator.remove();
					String oneRight = "", twoRight = "";
					for (int i = 0; i < rights.length; i++) {
						if (!rights[i].startsWith(left.toString())) {
							oneRight += (rights[i]
									.concat(left.toString() + "'") + "|");
						} else {
							twoRight += (rights[i].replaceFirst(
									left.toString(), "").concat(
									left.toString() + "'") + "|");
						}
					}
					listIterator.add(new Grammar(left.toString(), oneRight
							.substring(0, oneRight.length() - 1)));
					listIterator.add(new Grammar(left.toString() + "'",
							twoRight.concat("epsilon")));
				}
			}
		}
	}
