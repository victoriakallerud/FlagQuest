package com.flagquest.game.models

import com.badlogic.gdx.Gdx
import org.json.JSONArray
import java.io.File


data class  Country (val name: String, val region: String)
data class Question (val description: String, val answerOptions: List<AnswerOption>)
data class AnswerOption (val description: String, val isCorrect: Boolean)
data class Quiz (val questions: List<Question>)
class LocalApiModel {

    fun getFlagFilePathByCountryName(countryName: String): String {
        val countryCode = countryCodes[countryName]
        return "flags/${countryCode?.lowercase()}.png"
    }

    fun generateQuiz(numberOfQuestions: Int, region: String): Quiz {
        val questions = generateNumberOfQuestions(numberOfQuestions, region)
        return Quiz(questions)
    }

    private fun getAnswerOptionsByRegion(region: String): List<String> {
        val countriesJson = Gdx.files.internal("countries.json").readString()
        val countries = parseCountriesFromJson(countriesJson)
        val countriesInRegion: List<Country> = countries.filter { it.region == region }
        return countriesInRegion.shuffled().take(4).map { it.name    }
    }

    fun generateQuestion(region: String): Question {
        var answerOptions: List<String> = getAnswerOptionsByRegion(region)
        val correctAnswer = answerOptions[0]
        answerOptions = answerOptions.shuffled()
        val desc = correctAnswer
        return Question(desc, answerOptions.map { AnswerOption(it, it == correctAnswer) })
    }

    private fun generateNumberOfQuestions(numberOfQuestions: Int, region: String): List<Question> {
        return List(numberOfQuestions) { generateQuestion(region) }
    }

    private fun parseCountriesFromJson(json: String): List<Country> {
        val countries = mutableListOf<Country>()
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val name = jsonObj.getString("name")
            val region = jsonObj.getString("region")
            countries.add(Country(name, region))
        }
        return countries
    }

    private val countryCodes = mapOf(
        "Afghanistan" to "AF",
        "Albania" to "AL",
        "Algeria" to "DZ",
        "Andorra" to "AD",
        "Angola" to "AO",
        "Antigua and Barbuda" to "AG",
        "Argentina" to "AR",
        "Armenia" to "AM",
        "Australia" to "AU",
        "Austria" to "AT",
        "Azerbaijan" to "AZ",
        "Bahamas" to "BS",
        "Bahrain" to "BH",
        "Bangladesh" to "BD",
        "Barbados" to "BB",
        "Belarus" to "BY",
        "Belgium" to "BE",
        "Belize" to "BZ",
        "Benin" to "BJ",
        "Bhutan" to "BT",
        "Bolivia" to "BO",
        "Bosnia and Herzegovina" to "BA",
        "Botswana" to "BW",
        "Brazil" to "BR",
        "Brunei" to "BN",
        "Bulgaria" to "BG",
        "Burkina Faso" to "BF",
        "Burundi" to "BI",
        "Cabo Verde" to "CV",
        "Cambodia" to "KH",
        "Cameroon" to "CM",
        "Canada" to "CA",
        "Central African Republic" to "CF",
        "Chad" to "TD",
        "Chile" to "CL",
        "China" to "CN",
        "Colombia" to "CO",
        "Comoros" to "KM",
        "Congo" to "CG",
        "Costa Rica" to "CR",
        "Croatia" to "HR",
        "Cuba" to "CU",
        "Cyprus" to "CY",
        "Czech Republic" to "CZ",
        "Democratic Republic of the Congo" to "CD",
        "Denmark" to "DK",
        "Djibouti" to "DJ",
        "Dominica" to "DM",
        "Dominican Republic" to "DO",
        "East Timor" to "TL",
        "Ecuador" to "EC",
        "Egypt" to "EG",
        "El Salvador" to "SV",
        "Equatorial Guinea" to "GQ",
        "Eritrea" to "ER",
        "Estonia" to "EE",
        "Eswatini" to "SZ",
        "Ethiopia" to "ET",
        "Fiji" to "FJ",
        "Finland" to "FI",
        "France" to "FR",
        "Gabon" to "GA",
        "Gambia" to "GM",
        "Georgia" to "GE",
        "Germany" to "DE",
        "Ghana" to "GH",
        "Greece" to "GR",
        "Grenada" to "GD",
        "Guatemala" to "GT",
        "Guinea" to "GN",
        "Guinea-Bissau" to "GW",
        "Guyana" to "GY",
        "Haiti" to "HT",
        "Honduras" to "HN",
        "Hungary" to "HU",
        "Iceland" to "IS",
        "India" to "IN",
        "Indonesia" to "ID",
        "Iran" to "IR",
        "Iraq" to "IQ",
        "Ireland" to "IE",
        "Israel" to "IL",
        "Italy" to "IT",
        "Ivory Coast" to "CI",
        "Jamaica" to "JM",
        "Japan" to "JP",
        "Jordan" to "JO",
        "Kazakhstan" to "KZ",
        "Kenya" to "KE",
        "Kiribati" to "KI",
        "Kosovo" to "XK",
        "Kuwait" to "KW",
        "Kyrgyzstan" to "KG",
        "Laos" to "LA",
        "Latvia" to "LV",
        "Lebanon" to "LB",
        "Lesotho" to "LS",
        "Liberia" to "LR",
        "Libya" to "LY",
        "Liechtenstein" to "LI",
        "Lithuania" to "LT",
        "Luxembourg" to "LU",
        "Madagascar" to "MG",
        "Malawi" to "MW",
        "Malaysia" to "MY",
        "Maldives" to "MV",
        "Mali" to "ML",
        "Malta" to "MT",
        "Marshall Islands" to "MH",
        "Mauritania" to "MR",
        "Mauritius" to "MU",
        "Mexico" to "MX",
        "Micronesia" to "FM",
        "Moldova" to "MD",
        "Monaco" to "MC",
        "Mongolia" to "MN",
        "Montenegro" to "ME",
        "Morocco" to "MA",
        "Mozambique" to "MZ",
        "Myanmar" to "MM",
        "Namibia" to "NA",
        "Nauru" to "NR",
        "Nepal" to "NP",
        "Netherlands" to "NL",
        "New Zealand" to "NZ",
        "Nicaragua" to "NI",
        "Niger" to "NE",
        "Nigeria" to "NG",
        "North Korea" to "KP",
        "North Macedonia" to "MK",
        "Norway" to "NO",
        "Oman" to "OM",
        "Pakistan" to "PK",
        "Palau" to "PW",
        "Palestine" to "PS",
        "Panama" to "PA",
        "Papua New Guinea" to "PG",
        "Paraguay" to "PY",
        "Peru" to "PE",
        "Philippines" to "PH",
        "Poland" to "PL",
        "Portugal" to "PT",
        "Qatar" to "QA",
        "Romania" to "RO",
        "Russia" to "RU",
        "Rwanda" to "RW",
        "Saint Kitts and Nevis" to "KN",
        "Saint Lucia" to "LC",
        "Saint Vincent and the Grenadines" to "VC",
        "Samoa" to "WS",
        "San Marino" to "SM",
        "Sao Tome and Principe" to "ST",
        "Saudi Arabia" to "SA",
        "Senegal" to "SN",
        "Serbia" to "RS",
        "Seychelles" to "SC",
        "Sierra Leone" to "SL",
        "Singapore" to "SG",
        "Slovakia" to "SK",
        "Slovenia" to "SI",
        "Solomon Islands" to "SB",
        "Somalia" to "SO",
        "South Africa" to "ZA",
        "South Korea" to "KR",
        "South Sudan" to "SS",
        "Spain" to "ES",
        "Sri Lanka" to "LK",
        "Sudan" to "SD",
        "Suriname" to "SR",
        "Sweden" to "SE",
        "Switzerland" to "CH",
        "Syria" to "SY",
        "Taiwan" to "TW",
        "Tajikistan" to "TJ",
        "Tanzania" to "TZ",
        "Thailand" to "TH",
        "Togo" to "TG",
        "Tonga" to "TO",
        "Trinidad and Tobago" to "TT",
        "Tunisia" to "TN",
        "Turkey" to "TR",
        "Turkmenistan" to "TM",
        "Tuvalu" to "TV",
        "Uganda" to "UG",
        "Ukraine" to "UA",
        "United Arab Emirates" to "AE",
        "United Kingdom" to "GB",
        "United States" to "US",
        "Uruguay" to "UY",
        "Uzbekistan" to "UZ",
        "Vanuatu" to "VU",
        "Vatican City" to "VA",
        "Venezuela" to "VE",
        "Vietnam" to "VN",
        "Yemen" to "YE",
        "Zambia" to "ZM",
        "Zimbabwe" to "ZW"
    )


}