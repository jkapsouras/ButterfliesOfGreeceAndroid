package gr.jkapsouras.butterfliesofgreece.extensions

import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto

fun List<ButterflyPhoto>.uniques() : List<ButterflyPhoto>{
    return  this.distinctBy { it.source }
}