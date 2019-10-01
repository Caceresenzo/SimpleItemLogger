package caceresenzo.frameworks.database.setup.sql;

public class SqliteTableBuilder extends SqlTableBuilder {
	
	@Override
	public String getKeywordForFlag(int flag) {
		switch (flag) {
			case FLAG_AUTO_INCREMENT: {
				return "AUTOINCREMENT";
			}
			
			default: {
				return super.getKeywordForFlag(flag);
			}
		}
	}
	
}