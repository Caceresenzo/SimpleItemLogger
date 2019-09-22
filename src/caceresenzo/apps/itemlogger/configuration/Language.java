package caceresenzo.apps.itemlogger.configuration;

import caceresenzo.libs.internationalization.HardInternationalization;
import caceresenzo.libs.internationalization.i18n;

public class Language {
	
	public static final String LANGUAGE_FRENCH = "Français";
	
	private static Language LANGUAGE;
	private HardInternationalization selected = null;
	
	private Language() {
		selected = new French();
	}
	
	public void initialize() {
		i18n.setSelectedLanguage(LANGUAGE_FRENCH);
	}
	
	private class French extends HardInternationalization {
		
		public French() {
			super();
			register(LANGUAGE_FRENCH);
		}
		
		@Override
		public void set() {
			o("multiple-element-letter", "s");

			o("application.title", "Simple Carnet d'Objet");

			o("logger.panel.search.title", "Recherche");
			o("logger.panel.search.filters.title", "Filtres");
			o("logger.panel.action.title", "Actions");
			o("logger.panel.data.title", "Données");
			o("logger.panel.data.title.with", "Données — %s");
			o("logger.panel.data.title.with.part.item", "Inventaire");
			o("logger.panel.data.title.with.part.person", "Personnes");
			o("logger.panel.data.title.with.part.historyentry", "Historique");

			o("logger.button.search", "Rechercher");

			o("logger.button.action.add", "Ajouter");
			o("logger.button.action.items", "Inventaire");
			o("logger.button.action.persons", "Personnes");
			o("logger.button.action.history", "Historique");
			o("logger.button.action.print", "Imprimer");
			
			o("logger.table.column.id", "ID");
			o("logger.table.column.name", "Nom");
			o("logger.table.column.quantity", "Quantité");
			o("logger.table.column.firstname", "Prénom");
			o("logger.table.column.lastname", "Nom de Famille");
			o("logger.table.column.phone", "Numéro de Téléphone");

			o("create-dialog.button.done", "Valider");
			o("create-dialog.button.cancel", "Annuler");
			
			o("application.copyright.full", "SIMPLE CARNET D'OBJET\nCRÉE PAR ENZO CACERES POUR L'ENTREPRISE NEGRO SA");
			
			o("", "");
		}
		
	}
	
	public HardInternationalization getSelected() {
		return selected;
	}
	
	public static Language get() {
		if (LANGUAGE == null) {
			LANGUAGE = new Language();
		}
		
		return LANGUAGE;
	}
	
	public static HardInternationalization getActual() {
		return get().getSelected();
	}
	
}