package gr.jkapsouras.butterfliesofgreece.fragments.contribute.state

import gr.jkapsouras.butterfliesofgreece.dto.ContributionItem

class ContributeState(
    val contributionItem: ContributionItem,
    val exportedHtml: String,
    val pdfData: String?){}

    fun ContributeState.with(pdfData:String? = null, name:String? = null, date:String? = null, altitude:String? = null,
                             place:String? = null, longitude:String? = null, latitude:String? = null, stage:String? = null,
                             genusSpecies:String? = null, nameSpecies:String? = null, comments: String? = null, exportedHtml: String? = null) : ContributeState {
        val item = this.contributionItem.with(
            name = name ?: this.contributionItem.name,
            date = date ?: this.contributionItem.date,
            altitude = altitude ?: this.contributionItem.altitude,
            place = place ?: this.contributionItem.place,
            longitude = longitude ?: this.contributionItem.longitude,
            latitude = latitude ?: this.contributionItem.latitude,
            stage = stage ?: this.contributionItem.stage,
            genusSpecies = genusSpecies ?: this.contributionItem.genusSpecies,
            nameSpecies = nameSpecies ?: this.contributionItem.nameSpecies,
            comments = comments ?: this.contributionItem.comments
        )
        return ContributeState(
            contributionItem = item,
            exportedHtml = exportedHtml ?: this.exportedHtml,
            pdfData = pdfData ?: this.pdfData
        )
    }

    fun ContributeState.prepareHtmlForExport(items: List<ContributionItem>) : ContributeState {
        var xdatarows: String = ""
        val xdata: String =
            "<html><head><style>table{border-collapse:collapse;width:100%}td,th{text-align:left;padding:8px;}tr:nth-child(even){background-color:#f2f2f2; -webkit-print-color-adjust: exact; }th{background-color:#4CAF50;color:#fff; -webkit-print-color-adjust: exact; }</style></head><body><table><tr><th>Photo Name</th><th>Date</th><th>Altitude</th><th>Place</th><th>Longtitude</th><th>Latitude</th><th>Stage</th><th>Genus Spieces</th><th>Name Spieces</th><th>Comments</th></tr>"
        //add the date field in table
        for (item in items) {
            xdatarows += "<tr><td>${item.name ?: ""}</td><td>${item.date ?: ""}</td><td>${item.altitude ?: ""}</td><td>${item.place ?: ""}</td><td>${item.longitude ?: ""}</td><td>${item.latitude ?: ""}</td><td>${item.stage ?: ""}</td><td>${item.genusSpecies ?: ""}</td><td>${item.nameSpecies ?: ""}</td><td>${item.comments ?: ""}</td></tr>"
        }
        val xdataend: String = "</table></body></html>";
        val xdatafinal = xdata + xdatarows + xdataend;
        return this.with(exportedHtml = xdatafinal)
    }
