package modules.escola.dao;

import modules.escola.beans.TipoTurma;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

import java.util.List;

public class TipoTurmaDao extends HQLProvider {

	private static final String  DEFAULT_ORDER = asc("nome");


	public static TipoTurma getById(int id){
		return Dao.uniqueResult(TipoTurma.class, field("id").equalsTo(id));
	}

	public static List<TipoTurma> listAll() {
		return Dao.list(TipoTurma.class,null, DEFAULT_ORDER);
	}
}