package caceresenzo.apps.itemlogger.configuration;

import caceresenzo.libs.internationalization.HardInternationalization;
import caceresenzo.libs.internationalization.i18n;

public class Language {
	
	/* Constants */
	public static final String LANGUAGE_FRENCH = "Français";
	
	/* Singleton */
	private static Language INSTANCE;
	
	/* Constructor */
	private Language() {
		new French();
	}
	
	/** Initialize the language to the {@link i18n} system. */
	public void initialize() {
		i18n.setSelectedLanguage(LANGUAGE_FRENCH);
	}
	
	private class French extends HardInternationalization {
		
		/* Constructor */
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
			o("logger.panel.data.title.with.part.constructionsite", "Chantiers");
			o("logger.panel.data.title.with.part.historyentry", "Historique");
			
			o("logger.button.search", "Rechercher");
			
			o("logger.button.action.add", "Ajouter");
			o("logger.button.action.items", "Inventaire");
			o("logger.button.action.persons", "Personnes");
			o("logger.button.action.construction-sites", "Chantiers");
			o("logger.button.action.history", "Historique");
			o("logger.button.action.print", "Imprimer");
			
			o("logger.table.column.id", "ID");
			o("logger.table.column.name", "Nom");
			o("logger.table.column.quantity", "Quantité");
			o("logger.table.column.stock", "En Stock");
			o("logger.table.column.firstname", "Prénom");
			o("logger.table.column.lastname", "Nom de Famille");
			o("logger.table.column.phone", "Numéro de Téléphone");
			o("logger.table.column.address", "Adresse");
			o("logger.table.column.item", "Objet");
			o("logger.table.column.person", "À");
			o("logger.table.column.construction_site", "Pour le chantier");
			o("logger.table.column.lend", "Prêt");
			o("logger.table.column.return", "Retour");
			
			o("create-dialog.window.title", "Ajout d'une ligne");
			o("create-dialog.frame.new-line-part", "Nouvelle ligne — %s");
			o("create-dialog.field.optional", "(optionel)");
			o("create-dialog.button.done", "Valider");
			o("create-dialog.button.cancel", "Annuler");
			o("create-dialog.combobox.default.select-an-item", "Selectionner un élément");
			o("create-dialog.error.dialog.title", "Champs manquant");
			o("create-dialog.error.dialog.message", "Certains champs sont manquants ou n'ont pas été traités correctement.\nSi vous pensez que tout est correct, et que ce n'est pas un bug, contactez le développeur.\n\nDétails:\n    - Champs : \"%s\"\n    - Erreur : %s");
			
			o("model.item", "Item");
			o("model.person", "Personne");
			o("model.constructionsite", "Chantier");
			o("model.historyentry", "Entrée d'Historique");
			
			o("application.copyright.full", "SIMPLE CARNET D'OBJET\nCRÉE PAR ENZO CACERES POUR L'ENTREPRISE NEGRO SA");
			
			o("", "");
		}
		
	}
	
	/** @return Language's singleton instance. */
	public static final Language get() {
		if (INSTANCE == null) {
			INSTANCE = new Language();
		}
		
		return INSTANCE;
	}
	
}