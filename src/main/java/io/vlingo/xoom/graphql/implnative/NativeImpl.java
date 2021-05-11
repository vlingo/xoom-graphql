package io.vlingo.xoom.graphql.implnative;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.graphql.GraphQLProcessor;
import io.vlingo.xoom.graphql.GraphQLProcessorActor;
import io.vlingo.xoom.graphql.GraphQLResource;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

import java.util.Collections;

public final class NativeImpl {
  @CEntryPoint(name = "Java_io_vlingo_xoom_graphqlnative_Native_start")
  public static int start(@CEntryPoint.IsolateThreadContext long isolateId, CCharPointer name) {
    final String nameString = CTypeConversion.toJavaString(name);
    World world = World.startWithDefaults(nameString);


    final GraphQLProcessorActor.GraphQLProcessorInstantiator instantiator = new GraphQLProcessorActor.GraphQLProcessorInstantiator(
        "schema.graphqls", Collections.emptyList());

    final Definition definition = Definition.has(GraphQLProcessorActor.class, instantiator);
    final GraphQLProcessor processor = world.stage().actorFor(GraphQLProcessor.class, definition);
    final GraphQLResource graphQLResource = new GraphQLResource(world.stage(), processor);
    return 0;
  }
}
