package gr.jkapsouras.butterfliesofgreece.dto

data class ContributionItem(
    val name:String?,
    val date:String?,
    val altitude:String?,
    val place:String?,
    val longitude:String?,
    val latitude:String?,
    val stage:String?,
    val genusSpecies:String?,
    val nameSpecies:String?,
    val comments :String?)
{
    fun with(name:String? = null, date:String? = null, altitude:String? = null, place:String? = null, longitude:String? = null,
             latitude:String? = null, stage:String? = null, genusSpecies:String? = null, nameSpecies:String? = null, comments: String? = null) : ContributionItem{
    return ContributionItem(name = name ?: this.name,
    date = date ?: this.date,
    altitude = altitude ?: this.altitude,
    place = place ?: this.place,
    longitude = longitude ?: this.longitude,
    latitude = latitude ?: this.latitude,
    stage = stage ?: this.stage,
    genusSpecies =  genusSpecies ?: this.genusSpecies,
    nameSpecies = nameSpecies ?: this.nameSpecies,
    comments = comments ?: this.comments)
}
}