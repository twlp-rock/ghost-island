<template>
    <b-container fluid>
        <h1>Ghost Island  <font-awesome-icon icon="ghost" v-on:click="loadSpotData" /></h1>
        <b-row class="text-center">
            <b-col cols="12" class="py-3">
                <b-form-input v-model.lazy="word" type="text" placeholder="Enter the keyword" />
            </b-col>
        </b-row>
        <b-row class="text-right" style="height: 30px">
            <b-col cols="12" class="py-3">
                <b-spinner v-if="loading" type="grow" label="Loading..." />
            </b-col>
        </b-row>
        <b-row class="text-center">
            <b-col cols="12" class="py-3">
                <svg id="map" width="100%" viewBox="0 0 800 600"></svg>
            </b-col>
        </b-row>
    </b-container>
</template>

<script lang="ts">
    import * as d3 from "d3";
    import * as topojson from "topojson";
    let projection;
    let svgContainer;
    export default{
        name: "Map",
        data() {
            return {
                word: '',
                loading: false,
                lineOfficeLocation: [121.565630,25.0805452]
            }
        },
        mounted() {
            let self = this;
            projection = d3.geoMercator().center([121,24]).scale(8600);
            svgContainer = d3.select("svg#map");
            svgContainer.on('mousedown', function() {
                self.loadNearSpotData(projection.invert(d3.mouse(this)));
            });
            this.renderingTaiwanMap();
            this.loadNearSpotData(this.lineOfficeLocation);
        },
        methods: {
            async renderingTaiwanMap(){
                let topodata = await d3.json("/data-source/country.json");
                let features = topojson.feature(topodata, topodata["objects"]["COUNTY_MOI_1070516"]).features;
                let path = d3.geoPath().projection(
                    projection
                );
                
                svgContainer.selectAll("path")
                    .data(features)
                    .enter()
                    .append("path")
                    .attr("d", path)
                    .attr("fill", "#666666");

                let spot = {
                    coordinates: projection([121.565694,25.080618])
                };
                svgContainer.append("circle")
                    .attr("fill", "#00B901")
                    .attr("cx", spot.coordinates[0])
                    .attr("cy", spot.coordinates[1])
                    .attr("r", 2);
            },
            async loadSpotData(){
                let self = this;
                self.loading = true;
                fetch("/api/spots")
                .then(function(response){
                    return response.json()
                })
                .then(function(json){
                    // console.log(json);
                    let path = d3.geoPath().projection(
                        projection
                    );

                    svgContainer.selectAll("circle")
                        .remove();
                    
                    for ( let i = 0 ; i < json.length ; ++i ){
                        let orgCoordinates = json[i].location.coordinates;
                        let spot = {
                            coordinates: projection(orgCoordinates)
                        };

                        svgContainer.append("circle")
                            .attr("fill", "rgba(255,0,0,0.01)")
                            .attr("cx", spot.coordinates[0])
                            .attr("cy", spot.coordinates[1])
                            .attr("r", 1);
                    }

                    self.loading = false;
                });
            },
            async loadNearSpotData(location){
                let self = this;
                self.loading = true;
                fetch("/api/spots?filter[where][location][near]="+location[0]+","+location[1]+"&filter[where][location][maxDistance]=10&filter[where][location][unit]=kilometers")
                .then(function(response){
                    return response.json()
                })
                .then(function(json){
                    // console.log(json);
                    let path = d3.geoPath().projection(
                        projection
                    );

                    svgContainer.selectAll("circle")
                        .remove();
                    
                    for ( let i = 0 ; i < json.length ; ++i ){
                        let orgCoordinates = json[i].location.coordinates;
                        let spot = {
                            coordinates: projection(orgCoordinates)
                        };

                        svgContainer.append("circle")
                            .attr("fill", "rgba(255,0,0,0.01)")
                            .attr("cx", spot.coordinates[0])
                            .attr("cy", spot.coordinates[1])
                            .attr("r", 1);
                    }

                    self.loading = false;
                });
            }
        },
        watch: {
            word: function(word){
                let self = this;
                if (word.trim().length==0){
                    return;
                }
                self.loading = true;
                fetch("/analysis/search?word="+word)
                .then(function(response){
                    return response.json();
                })
                .then(function(json){
                    return JSON.parse("[" + json + "]");
                })
                .then(function(json){

                    svgContainer.selectAll("circle")
                        .remove();
                    for ( let i = 0 ; i < json.length ; ++i ){
                        let orgCoordinates = json[i].location.coordinates;
                        let spot = {
                            coordinates: projection(orgCoordinates)
                        };

                        svgContainer.append("circle")
                            .attr("fill", "rgba(255,255,0,0.02)")
                            .attr("cx", spot.coordinates[0])
                            .attr("cy", spot.coordinates[1])
                            .attr("r", 2);
                    }
                    self.loading = false;
                });
            }
        }
    }
</script>