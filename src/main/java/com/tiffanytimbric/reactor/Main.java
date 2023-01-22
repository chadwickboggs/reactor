package com.tiffanytimbric.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.time.Duration;
import java.util.List;
import java.util.Optional;


public class Main {

    public static void main( @Nullable final String... args ) {
        System.out.println("Program Start");

        final Mono<Integer> zeroMono = Mono.just( 0 );
        final Flux<Integer> oddNumbersFlux = Flux.just( 1, 3, 5, 7 );
        final Flux<Integer> evenNumbersFlux = Flux.just( 2, 4, 6, 8 );

        final Optional<List<Integer>> allNumbersOpt = zeroMono
            .mergeWith( oddNumbersFlux )
            .mergeWith( evenNumbersFlux )
//            .subscribe( System.out::println )
            .collectList()
            .blockOptional( Duration.ofSeconds( 1 ) );

        System.out.println( "All Numbers: " + allNumbersOpt.get() );

        System.out.println("Program End");
    }

}
