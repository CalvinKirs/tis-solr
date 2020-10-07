<?xml version="1.0" encoding="UTF-8" ?>

<config>

  <luceneMatchVersion>8.6.1</luceneMatchVersion>
  <hdfsHome>${hdfsaddress}</hdfsHome>
  <tisRepository>${terminator_host_address}</tisRepository>


  <dataDir>./data</dataDir>

  <directoryFactory name="DirectoryFactory"
                    class="solr.NRTCachingDirectoryFactory">

  </directoryFactory>

  <codecFactory class="solr.SchemaCodecFactory"/>


  <schemaFactory class="ClassicIndexSchemaFactory"/>


  <indexConfig>

     <lockType>native</lockType>



     <infoStream>true</infoStream>
  </indexConfig>


  <jmx />
  <updateHandler class="org.apache.solr.update.TisDirectUpdateHandler2">

    <updateLog>
      <str name="dir">tlog</str>
      <str name="numRecordsToKeep">100</str>
      <str name="maxNumLogsToKeep">10</str>
    </updateLog>

     <autoCommit>
       <!--15min-->
       <maxTime>900000</maxTime>
       <maxDocs>99999999</maxDocs>
       <openSearcher>false</openSearcher>
     </autoCommit>



     <autoSoftCommit>
       <maxTime>6000</maxTime>
       <maxDocs>99999999</maxDocs>
     </autoSoftCommit>



  </updateHandler>



  <indexReaderFactory name="IndexReaderFactory"
     class="org.apache.solr.core.StandardIndexReaderFactory"/>



  <query>

    <maxBooleanClauses>1024</maxBooleanClauses>


    <filterCache class="solr.FastLRUCache"
                 size="512"
                 initialSize="512"
                 autowarmCount="0"/>


    <queryResultCache class="solr.LRUCache"
                     size="512"
                     initialSize="512"
                     autowarmCount="0"/>


    <documentCache class="solr.LRUCache"
                   size="512"
                   initialSize="512"
                   autowarmCount="0"/>


    <cache name="perSegFilter"
      class="solr.search.LRUCache"
      size="10"
      initialSize="0"
      autowarmCount="10"
      regenerator="solr.NoOpRegenerator" />


    <enableLazyFieldLoading>true</enableLazyFieldLoading>


   <queryResultWindowSize>20</queryResultWindowSize>

   <queryResultMaxDocsCached>200</queryResultMaxDocsCached>
    <listener event="newSearcher" class="solr.QuerySenderListener">
      <arr name="queries">
      </arr>
    </listener>
    <listener event="firstSearcher" class="solr.QuerySenderListener">
      <arr name="queries">
        <lst>
          <str name="q">static firstSearcher warming in solrconfig.xml</str>
        </lst>
      </arr>
    </listener>


    <useColdSearcher>false</useColdSearcher>


    <maxWarmingSearchers>2</maxWarmingSearchers>

  </query>



  <requestDispatcher handleSelect="false" >

    <requestParsers enableRemoteStreaming="true"
                    multipartUploadLimitInKB="2048000"
                    formdataUploadLimitInKB="2048"
                    addHttpRequestToContext="false"/>

    <httpCaching never304="true" />

  </requestDispatcher>

  <requestHandler name="/admin/file" class="org.apache.solr.handler.file.ShowFileRequestHandler" />

  <requestHandler name="/replication" class="com.qlangtech.tis.solrextend.handler.normal.TisReplicationHandler">
  </requestHandler>

  <requestHandler name="/select" class="solr.SearchHandler">
     <lst name="defaults">
       <str name="echoParams">explicit</str>
       <int name="rows">10</int>
       <str name="df">text</str>
     </lst>
    <arr name="last-components">
    </arr>
  </requestHandler>


  <requestHandler name="/query" class="solr.SearchHandler">
     <lst name="defaults">
       <str name="echoParams">explicit</str>
       <str name="wt">json</str>
       <str name="indent">true</str>
       <str name="df">text</str>
     </lst>
  </requestHandler>



  <updateRequestProcessorChain name="ignore-commit-from-client" default="true">
      <processor class="solr.IgnoreCommitOptimizeUpdateProcessorFactory">
      <int name="statusCode">403</int>
      <str name="responseMessage">Thou shall not issue a commit!</str>
      </processor>
      <processor class="solr.LogUpdateProcessorFactory" />
      <processor class="org.apache.solr.update.processor.TisDistributedUpdateProcessorFactory" />
      <processor class="solr.RunUpdateProcessorFactory" />
  </updateRequestProcessorChain>

  <queryResponseWriter name="json" class="solr.JSONResponseWriter">
    <str name="content-type">text/plain; charset=UTF-8</str>
  </queryResponseWriter>




  <!-- Legacy config for the admin interface -->
  <admin>
    <defaultQuery>*:*</defaultQuery>
  </admin>

</config>
