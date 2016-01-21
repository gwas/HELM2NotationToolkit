HELMNotationToolkit Requirements
================================

HELMNotationToolkit 1.0
-----------------------
* Java 1.6
* MarvinBeans 5.0


HELMNotationToolkit 1.1
-----------------------
* Java 1.6
* MarvinBeans 5.0


HELMNotationToolkit 1.2
-----------------------
* Java 1.6
* MarvinBeans 5.0

HELMNotationToolkit 1.3
-----------------------
* Java 1.7
* MarvinBeans 5.0


There are two new configuration files in .helm.

The Chemistry.property contains the information which Chemistry plugin you want to use. The default one is MARVIN.

chemistry.plugin=MARVIN

The MonomerStoreConfig.properties contains the information if you want to use a webservice to do monomer management actions.

use.webservice=false
update.automatic=true
webservice.monomers.url=http://localhost:8080
webservice.monomers.path=DemoWebserver/service/monomerStore?polymerType=#webservice.monomers.put.path=path/monomerToolkit
webservice.monomers.put.path=DemoWebserver/service/monomerStore
webservice.nucleotides.url=http://localhost:8080
webservice.nucleotides.path=DemoWebserver/service/nucleotideStore
#webservice.nucleotides.put.path=path/nucleotideTemplate
webservice.nucleotides.put.path=DemoWebserver/service/nucleotidestore
webservice.editor.categorization.url=http://localhost:8080
webservice.editor.categorization.path=path/monomerStoreEditorCategories
