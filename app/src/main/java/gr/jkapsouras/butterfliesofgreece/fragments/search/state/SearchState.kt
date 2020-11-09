package gr.jkapsouras.butterfliesofgreece.fragments.search.state

import gr.jkapsouras.butterfliesofgreece.dto.Specie

class SearchState(val term:String, val result: List<Specie>){

}

    fun SearchState.with(term:String? = null, result:List<Specie>? = null) : SearchState{
        return SearchState(term = term?: this.term, result = result?: this.result)
    }