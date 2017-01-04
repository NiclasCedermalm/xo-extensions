XO Analytics Provider for Local Database
============================================

A XO analytics provider for local SQL databases. Offers the possibility to have all tracking of content experiments in a local database instead of Google Analytics

## Local Analytics Provider

The local analytics provider is a fully functional analytics provider for Content Experiments. It can be used instead of Google Analytics.
It can use any JDBC database for storing and quering tracking data. The winner selection algorithm is also configurable. So the following algorithms
are available:

* Configurable Chi Square - A configurable version of standard Chi Square algorithm where minimum conversions per variant can be configured (property name: MinimumConversionsPerVariant)
* Reach Conversion Goal Algorithm - Simple algorithm that just make the variant that first reach a specific number of conversions to winner (property name: ConversionGoal ) 

For test and demos dummy data can be generated for a specific experiment.

Example of configuration in smarttarget_conf.xml:

```
<Analytics implementationClass="com.sdl.experienceoptimization.analytics.localdb.LocalAnalyticsManager" timeoutMilliseconds="5000" trackingRedirectUrl="/redirect/">
    <Storage url="jdbc:hsqldb:/db/tracking-db/tracking-db;user=user1;password=secret" className="org.hsqldb.jdbcDriver" cacheTime="10000"/>
    <ExperimentWinnerAlgorithmClassName>com.sdl.experienceoptimization.analytics.algorithm.ReachConversionGoalAlgorithm</ExperimentWinnerAlgorithmClassName>
    <ConversionGoal>10</ConversionGoal>
    <TrackingStoreInterval>10000</TrackingStoreInterval>
    <UseDummyData>true</UseDummyData>
    <Dummy experimentId="a9cf5654-c6ab-41ec-a8da-3b13de130c0c">
        <MinViews>230</MinViews>
        <MaxViews>300</MaxViews>
        <MinConversions>0</MinConversions>
        <MaxConversions>70</MaxConversions>
        <Winner>
            <Publication>tcm:0-3-1</Publication>
            <VariantIndex>1</VariantIndex>
            <MinConversions>50</MinConversions>
            <MaxConversions>100</MaxConversions>
        </Winner>
    </Dummy>
</Analytics>
```

## Database

The following table is needed to be created in you new/existing database:

```
CREATE TABLE TRACKING_RESULT(
    EXP_ID varchar(50) NOT NULL,
    PUBLICATION_ID int NOT NULL,
    PUB_TARGET_ID int NOT NULL,
    COMPONENT_ID int NOT NULL,
    COMPONENT_TEMPLATE_ID int NOT NULL,
    PAGE_ID int NOT NULL,
    REGION varchar(50) NOT NULL,
    TRACKING_DATE date NOT NULL,
    CHOSEN_VARIANT int NOT NULL,
    TRACKING_TYPE int NOT NULL,
    TRACKING_COUNT int NOT NULL)
GO
   
```

This is made for MS-SQL but can easily be modified for other RDMBS, such as Hypersonic, Oracle etc.