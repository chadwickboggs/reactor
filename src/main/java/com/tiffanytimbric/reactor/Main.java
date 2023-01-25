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
 *          type is analogous to event stream, event processing.
 *         </blockquote>
 *     </li>
 *     <li>
 *         <b>Pull:</b>
 *         <blockquote>
 *          A fixed block of data pre-exists before the processing of it begins.
 *          This flow type is analogous to Java Collections.
 *         </blockquote>
 *     </li>
 * </ul>
 * <p><p>
 * The Reactive eXtensions API supports two methods of data consumption,
 * blocking read, and subscription.  Blocking read blocks the reading thread.
 * Subscription read may be processed by a separate thread, thereby not blocking
 * the Flow construction thread.  In general, event handlers (push data flow)
 * use subscriptions instead of blocking reads.
 */
public class Main {

    public static void main( @Nullable final String... args ) {
        System.out.println( "Program Start\n" );

        System.out.println( "Pulling data..." );
        Flux<Integer> allNumbersFlux = createPullTheDataFlux();
        System.out.println( "Extracting data as list from Pull Data Flux..." );
        List<Integer> allNumbers = extractAsList( allNumbersFlux );
        System.out.println( "All Numbers: " + allNumbers );

        System.out.println( "Pulling data..." );
        allNumbersFlux = createPullTheDataFlux();
        System.out.println( "Subscribing to Pull Data Flux..." );
        allNumbersFlux.subscribe( System.out::println );

        System.out.println( "\nPushing data..." );
        allNumbersFlux = createPushTheDataFlux();
        System.out.println( "Subscribing to Push Data Flux..." );
        allNumbersFlux.subscribe( System.out::println );

        System.out.println( "\nProgram End" );
    }

    @NonNull
    private static List<Integer> extractAsList(
            @NonNull final Flux<Integer> allNumbersFlux
    ) {
        final Optional<List<Integer>> allNumbersOpt = allNumbersFlux
                .collectList()
                .blockOptional( Duration.ofSeconds( 1 ) );

        return allNumbersOpt.orElseGet( ArrayList::new );
    }

    @NonNull
    private static Flux<Integer> createPullTheDataFlux() {
        final Mono<Integer> zeroMono = Mono.just( 0 );
        final Flux<Integer> oddNumbersFlux = Flux.just( 1, 3, 5, 7 );
        final Flux<Integer> evenNumbersFlux = Flux.just( 2, 4, 6, 8 );

        return zeroMono
                .mergeWith( oddNumbersFlux )
                .mergeWith( evenNumbersFlux );
    }

    @NonNull
    private static Flux<Integer> createPushTheDataFlux() {
//        createEmitter( item -> System.out.println( item ), 1000l, 0 );
//        createEmitter( consumer, 1000l, 1, 3, 5, 7 );
//        createEmitter( consumer, 1000l, 2, 4, 6, 8 );

        return Flux.empty();
    }

}
