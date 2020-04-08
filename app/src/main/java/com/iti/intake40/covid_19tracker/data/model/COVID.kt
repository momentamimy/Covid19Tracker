package com.iti.intake40.covid_19tracker.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 data class COVID (
    @Expose
    @SerializedName("country_name")
    val countryName: String
    ,
    @Expose
    @SerializedName("cases")
    val cases: String,

    @Expose
    @SerializedName("deaths")
    val deaths: String,

    @Expose
    @SerializedName("region")
    val region: String,

    @Expose
    @SerializedName("total_recovered")
    val recover: String,

    @Expose
    @SerializedName("new_deaths")
    val newDeaths: String,

    @Expose
    @SerializedName("new_cases")
    val newCases: String,

    @Expose
    @SerializedName("serious_critical")
    val seriousCritical: String,

    @Expose
    @SerializedName("active_cases")
    val activeCases: String,

    @Expose
    @SerializedName("total_cases_per_1m_population")
    val total_cases_per_1m_population: String
 )