mvn deploy:deploy-file ^
 -DgroupId=com.qlangtech.tis ^
 -DartifactId=tis-solr-core ^
 -Dversion=7.6.0-fix1 ^
 -Dpackaging=jar ^
 -DpomFile=D:\j2ee_solution\mvn_repository\org\apache\solr\solr-core\7.6.0\fix\solr-core-7.6.0.pom ^
 -Dsources=D:\j2ee_solution\mvn_repository\org\apache\solr\solr-core\7.6.0\fix\solr-core-7.6.0-sources.jar ^
 -Dfile=D:\j2ee_solution\mvn_repository\org\apache\solr\solr-core\7.6.0\fix\solr-core-7.6.0.jar ^
 -DrepositoryId=qlangtech ^
 -Durl=http://localhost:8080/release