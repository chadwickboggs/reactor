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
 * Reactive Streams defines pull data flow as a "cold" publisher, and the push
 * data flow as a "hot" publisher.
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

        demoDataPullWithBlockingRead();
        System.out.println();
        demoDataPullWithSubscriptionRead();
        System.out.println();
        demoDataPushWithSubscriptionRead();

        System.out.println( "\nProgram End" );
    }

    private static void demoDataPullWithBlockingRead() {
        System.out.println( "Demoing data pull with blocking read..." );

        System.out.println( "Pulling data..." );
        final Flux<Integer> allNumbersFlux = createPullTheDataFlux();

        System.out.println( "Extracting data as list from Pull Data Flux..." );
        final List<Integer> allNumbers = extractAsList( allNumbersFlux );
        System.out.println( allNumbers );
    }

    private static void demoDataPullWithSubscriptionRead() {
        System.out.println( "Demoing data pull with subscription read..." );

        System.out.println( "Pulling data..." );
        final Flux<Integer> allNumbersFlux = createPullTheDataFlux();

        System.out.println( "Subscribing to Pull Data Flux..." );
        allNumbersFlux.subscribe( System.out::println );
    }

    private static void demoDataPushWithSubscriptionRead() {
        System.out.println( "Demoing data push with subscription read..." );

        System.out.println( "\nPushing data..." );
        final Flux<Integer> allNumbersFlux = createPushTheDataFlux( 8 );

        System.out.println( "Subscribing to Push Data Flux..." );
        allNumbersFlux.subscribe( System.out::println );
        allNumbersFlux.publish().connect();
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
    private static Flux<Integer> createPushTheDataFlux( int maxValue ) {
        final Mono<Integer> zeroMono = Mono.just( 0 );

        final Flux<Integer> oddNumbersFlux = Flux.create( emitter -> new Thread( () -> {
            for ( int count = 1; count <= maxValue; count += 2 ) {
                emitter.next( count );
            }
        } ).start() );
        final Flux<Integer> evenNumbersFlux = Flux.create( emitter -> new Thread( () -> {
            for ( int count = 2; count <= maxValue; count += 2 ) {
                emitter.next( count );
            }
        } ).start() );

        return zeroMono
                .mergeWith( oddNumbersFlux )
                .mergeWith( evenNumbersFlux );
    }

}
