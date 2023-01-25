package com.tiffanytimbric.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * The Reactive eXtensions API supports two methods of data flow, push and pull.
 * The Java Streams API support supports only one method of data flow, pull.
 * <p><p>
 * <h>Data Flow Types</h3>
 * <ul>
 *     <li>
 *         <b>Push:</b>
 *         <blockquote>
 *          New data arrives asynchronously until the publisher signals its end.
 *          The subscriber process the incoming data as it arrives.  This flow
 *          type is alloigous to event stream, event processing.
 *         </blockquote>
 *     </li>
 *     <li>
 *         <b>Pull:</b>
 *         <blockquote>
 *          A fixed block of data pre-exists before the processing of it begins.
 *          This flow type is analigous to Java Collections.
 *         </blockquote>
 *     </li>
 * </ul>
 */
public class Main {

    public static void main( @Nullable final String... args ) {
        System.out.println("Program Start\n");

        System.out.println("Pulling data...");
        List<Integer> allNumbers = pullTheData();
        System.out.println( "All Numbers: " + allNumbers );

        System.out.println("\nPushing data...");
        allNumbers = pushTheData();
        System.out.println( "All Numbers: " + allNumbers );

        System.out.println("\nProgram End");
    }

    @NonNull
    private static List<Integer> pullTheData() {
        final Mono<Integer> zeroMono = Mono.just( 0 );
        final Flux<Integer> oddNumbersFlux = Flux.just( 1, 3, 5, 7 );
        final Flux<Integer> evenNumbersFlux = Flux.just( 2, 4, 6, 8 );

        final Optional<List<Integer>> allNumbersOpt = zeroMono
            .mergeWith( oddNumbersFlux )
            .mergeWith( evenNumbersFlux )
//            .subscribe( System.out::println )
            .collectList()
            .blockOptional( Duration.ofSeconds( 1 ) );

        if ( allNumbersOpt.isEmpty() ) {
            System.err.println("Error: No numbers returned.");

            System.exit( 1 );
        }

        return allNumbersOpt.get();
    }

    @NonNull
    private static List<Integer> pushTheData() {
//        createEmitter( item -> System.out.println( item ), 1000l, 0 );
//        createEmitter( consumer, 1000l, 1, 3, 5, 7 );
//        createEmitter( consumer, 1000l, 2, 4, 6, 8 );

        return new ArrayList<>();
    }

}
