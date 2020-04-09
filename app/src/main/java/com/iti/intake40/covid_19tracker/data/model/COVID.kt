package com.iti.intake40.covid_19tracker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "COVID")
 data class COVID (
    @Expose
    @ColumnInfo(name = "country_name")
    @SerializedName("country_name")
    @PrimaryKey
    val countryName: String
    ,
    @Expose
    @ColumnInfo(name = "cases")
    @SerializedName("cases")
    val cases: String,

    @Expose
    @ColumnInfo(name = "deaths")
    @SerializedName("deaths")
    val deaths: String,

    @Expose
    @ColumnInfo(name = "region")
    @SerializedName("region")
    val region: String,

    @Expose
    @ColumnInfo(name = "total_recovered")
    @SerializedName("total_recovered")
    val recover: String,

    @Expose
    @ColumnInfo(name = "new_deaths")
    @SerializedName("new_deaths")
    val newDeaths: String,

    @Expose
    @ColumnInfo(name = "new_cases")
    @SerializedName("new_cases")
    val newCases: String,

    @Expose
    @ColumnInfo(name = "serious_critical")
    @SerializedName("serious_critical")
    val seriousCritical: String,

    @Expose
    @ColumnInfo(name = "active_cases")
    @SerializedName("active_cases")
    val activeCases: String,

    @Expose
    @ColumnInfo(name = "total_cases_per_1m_population")
    @SerializedName("total_cases_per_1m_population")
    val total_cases_per_1m_population: String
 )