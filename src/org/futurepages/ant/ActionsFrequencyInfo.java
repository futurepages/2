package org.futurepages.ant;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.futurepages.ant.FileSearcher.Result;
import org.futurepages.core.ApplicationManager;
import org.futurepages.core.config.Params;
import org.futurepages.core.control.ActionConfig;
import org.futurepages.core.control.Controller;
import org.futurepages.util.ascii.TableBuilder;

@Deprecated
public class ActionsFrequencyInfo extends Task  {

	private String root;

	@Override
	public void execute() throws BuildException {

		try {
			Params.initialize("");
			Controller instance = new Controller();
			instance.offLineInit();
			ApplicationManager appManager = instance.getAppManager();
			Map<String, ActionConfig> actions = appManager.getActions();

			FileSearcher searcher = new FileSearcher();
			searcher.init(root);

			Set<String> keySet = actions.keySet();
			Map<String, Result> search = searcher.search(keySet);
//			Map<String, Result> search = searcher.searchFast(keySet);
			List<Result> resultadosOrdenados = new LinkedList<Result>(search.values());
			Collections.sort(resultadosOrdenados);

			printResults(resultadosOrdenados, actions);
		} catch (Exception e) {
			throw new BuildException(e.getMessage());
		}
	}


	public static void main(String[] args) {
		TableBuilder tb = TableBuilder.init()
				.head()
					.addColumn("Frequência")
					.addColumn("Action Id")
					.addColumn("Action Class")
				.end();
			tb
			.body()
				.addLine()
					.addCell("Freucnaia 1")
					.addCell("Action Id 1")
					.addCell("Action Class 1")
				.end()
			.end()
		.print();
	}

	private void printResults(List<Result> resultadosOrdenados, Map<String, ActionConfig> actions) {
		TableBuilder tb = TableBuilder.init()
				.head()
					.addColumn("Frequência")
					.addColumn("Action Id")
					.addColumn("Action Class")
				.end();
		for (Result result : resultadosOrdenados) {
			tb
			.body()
				.addLine()
					.addCell(String.valueOf(result.qtdFound()))
					.addCell(result.pivo)
					.addCell(actions.get(result.pivo).getActionClass().getCanonicalName())
				.end()
			.end();

		}
		tb.print();


//		printLineSeparator(false, colSpan_1,colSpan_2,colSpan_3);
//		printCell("Frequência", colSpan_1);
//		printCell("Action_ID", colSpan_2);
//		printCell("Action Class", colSpan_3);
//		printLineSeparator(colSpan_1,colSpan_2,colSpan_3);
//		for (Result result : resultadosOrdenados) {
//			if(!separacaoZeradosRealizada && result.qtdFound() > 0 ){
//				printLineSeparator(colSpan_1,colSpan_2,colSpan_3);
//				separacaoZeradosRealizada = true;
//			}
//			printCell(String.valueOf(result.qtdFound()), colSpan_1);
//			printCell(String.valueOf(result.pivo), colSpan_2);
//			printCell(actions.get(result.pivo).getActionClass().getCanonicalName(), colSpan_3);
//			System.out.println();
//		}
	}
//	private void breakline(){
//		System.out.println();
//	}
//
//	private void printLineSeparator(int... cellsSpan) {
//		printLineSeparator(true, cellsSpan);
//	}
//	private void printLineSeparator(boolean separator, int... cellsSpan) {
//		for (int i : cellsSpan) {
//			printLineCellDiv(separator, i);
//		}
//		breakline();
//	}


	public String getRoot() {
		return root;
	}


	public void setRoot(String root) {
		this.root = root;
	}



}
