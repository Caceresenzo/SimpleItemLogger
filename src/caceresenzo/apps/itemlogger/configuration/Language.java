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
			
			o("application.title", "Negro MAT.");
			o("application.copyright.full", getTranslation().getString("application.title").toUpperCase() + "\nCRÉE PAR ENZO CACERES POUR L'ENTREPRISE NEGRO SA");
			
			o("logger.panel.search.title", "Recherche");
			o("logger.panel.search.filters.title", "Filtres");
			o("logger.panel.action.title", "Actions");
			o("logger.panel.data.title", "Données");
			o("logger.panel.data.title.with", "Données — %s");
			o("logger.panel.data.title.with.part.item", "Materiel");
			o("logger.panel.data.title.with.part.person", "Personnes");
			o("logger.panel.data.title.with.part.constructionsite", "Chantiers");
			o("logger.panel.data.title.with.part.historyentry", "Historique");
			
			o("logger.button.search", "Rechercher");
			o("logger.button.clear-search", "Vider la rech.");
			
			o("logger.button.action.add", "Ajouter");
			o("logger.button.action.items", "Materiel");
			o("logger.button.action.persons", "Personnes");
			o("logger.button.action.construction-sites", "Chantiers");
			o("logger.button.action.history", "Historique");
			o("logger.button.action.export-pdf", "Exporter en PDF");
			
			o("logger.table.column.id", "ID");
			o("logger.table.column.name", "Nom");
			o("logger.table.column.initial_stock", "Stock Initial");
			o("logger.table.column.current_stock", "Stock Actuel");
			o("logger.table.column.corporation", "Société");
			o("logger.table.column.phone", "Numéro de Téléphone");
			o("logger.table.column.address", "Adresse");
			o("logger.table.column.item", "Objet");
			o("logger.table.column.person", "À");
			o("logger.table.column.quantity", "Quantité");
			o("logger.table.column.construction_site", "Chantier");
			o("logger.table.column.lend", "Prêt");
			o("logger.table.column.return", "Retour");
			o("logger.table.column.extra", "Commentaire(s)");
			o("logger.table.column.actions", "Actions");
			o("logger.table.column.actions.button.remove", "SUPPRIMER");
			o("logger.table.column.actions.button.remove.confirm-dialog.title", "Confirmation de suppression");
			o("logger.table.column.actions.button.remove.confirm-dialog.message", "Attention : Cette action n'est pas reversible !\nÊtes-vous sur de vouloir supprimer cette ligne ?");
			
			o("create-dialog.window.title", "Ajout d'une ligne");
			o("create-dialog.frame.new-line-part", "Nouvelle ligne — %s");
			o("create-dialog.field.optional", "(optionel)");
			o("create-dialog.button.done", "Valider");
			o("create-dialog.button.cancel", "Annuler");
			o("create-dialog.combobox.default.select-an-item", "Selectionner un élément");
			o("create-dialog.error.dialog.title", "Champs manquant");
			o("create-dialog.error.dialog.message", "Certains champs sont manquants ou n'ont pas été traités correctement.\nSi vous pensez que tout est correct, et que ce n'est pas un bug, contactez le développeur.\n\nDétails:\n    - Champs : \"%s\"\n    - Erreur : %s");
			
			o("export-dialog.window.title", "Exporter vers un PDF");
			o("export-dialog.file-chooser.title", "Selectionner le fichier de destination...");
			o("export-dialog.panel.settings.title", "Paramètres");
			o("export-dialog.panel.file.title", "Fichier");
			o("export-dialog.button.browse", "Explorer...");
			o("export-dialog.button.do-export", "Exporter");
			o("export-dialog.button.cancel", "Annuler");
			o("export-dialog.button.toggle.enabled", "ACTIVÉ");
			o("export-dialog.button.toggle.disabled", "DÉSACTIVÉ");
			o("export-dialog.dialog.error.nothing-enabled.title", "Paramètres invalides");
			o("export-dialog.dialog.error.nothing-enabled.message", "Veuillez selectionner au moins un ou plusieurs éléments à exporter.");
			o("export-dialog.dialog.error.export-error.title", "Exportation en erreur");
			o("export-dialog.dialog.error.export-error.message", "L'exportation a renvoyé une erreur.\nSi vous pensez que ce n'est pas normal, contactez le developpeur.\n\nErreur : %s");
			o("export-dialog.dialog.open-file.title", "Ouvrir le fichier");
			o("export-dialog.dialog.open-file.message", "Voulez vous ouvrir le fichier ?\nEmplacement : %s");
			
			o("model.item", "Item");
			o("model.person", "Personne");
			o("model.constructionsite", "Chantier");
			o("model.historyentry", "Entrée d'Historique");
			
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