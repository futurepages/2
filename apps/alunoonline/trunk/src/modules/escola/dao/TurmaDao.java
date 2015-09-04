package modules.escola.dao;

import modules.escola.beans.Turma;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

import java.util.List;


public class TurmaDao extends HQLProvider {

	private static final String  DEFAULT_ORDER = asc("codigo");

	public static List<Turma> listAll() {
		return Dao.list(Turma.class, null, DEFAULT_ORDER);
	}

	public static Turma getComMesmoCodigoDesta(Turma turma) {
		return Dao.uniqueResult(Turma.class, ands(
												field("id").differentFrom(turma.getId()),
											    field("codigo").equalsTo(turma.getCodigo())
								)
		);
	}
}
