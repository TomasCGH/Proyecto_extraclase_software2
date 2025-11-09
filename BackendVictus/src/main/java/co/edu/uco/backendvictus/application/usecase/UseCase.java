package co.edu.uco.backendvictus.application.usecase;

@FunctionalInterface
public interface UseCase<I, O> {

    O execute(I input);
}
