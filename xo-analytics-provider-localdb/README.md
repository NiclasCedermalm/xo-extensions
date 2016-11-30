XO Analytics Provider for Local Database
============================================

* Local DB Analytics Provider - offer the possibility to have all tracking of content experiments in a local SQL database instead of Google Analytics
* Possibility to enable automatic selection of winner in Content Experiments, i.e. after a winner has been selected that promotion is always shown for all visitors. This is configurable in the SmartTarget configuration.

## Local Analytics Provider

The local analytics provider is a fully functional analytics provider for Content Experiments. It can be used instead of Google Analytics.
It can use any JDBC database for storing and quering tracking data. The winner selection algorithm is also configurable. So the following algorithms
are available:

* Configurable Chi Square - A configurable version of standard Chi Square algorithm where minimum conversions per variant can be configured (property name: MinimumConversionsPerVariant)
* Reach Conversion Goal Algorithm - Simple algorithm that just make the variant that first reach a specific number of conversions to winner (property name: ConversionGoal ) 

For test and demos dummy data can be generated for a specific experiment.

Example of configuration in smarttarget_conf.xml:

```
<Analytics implementationClass="com.sdl.experienceoptimization.analytics.LocalAnalyticsManager" timeoutMilliseconds="5000" trackingRedirectUrl="/redirect/">
    <Storage url="jdbc:hsqldb:/db/tracking-db/tracking-db;user=user1;password=secret" className="org.hsqldb.jdbcDriver" cacheTime="10000"/>
    <ExperimentWinnerAlgorithmClassName>com.sdl.experienceoptimization.analytics.ReachConversionGoalAlgorithm</ExperimentWinnerAlgorithmClassName>
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
