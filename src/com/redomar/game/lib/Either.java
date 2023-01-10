package com.redomar.game.lib;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "OptionalGetWithoutIsPresent", "unused"})
public class Either<L, R> {

	private final Optional<L> left;
	private final Optional<R> right;

	public Either(final Optional<L> left, final Optional<R> right) {
		this.left = left;
		this.right = right;
	}

	@Contract("_ -> new")
	public static <L, R> @NotNull Either<L, R> left(final L value) {
		return new Either<>(Optional.of(value), Optional.empty());
	}

	@Contract("_ -> new")
	public static <L, R> @NotNull Either<L, R> right(final R value) {
		return new Either<>(Optional.empty(), Optional.ofNullable(value));
	}

	public static <T> Either<Exception, T> tryCatch(CheckedRunnable runnable) {
		try {
			runnable.run();
			return Either.right(null);
		} catch (Exception e) {
			return Either.left(e);
		}
	}

	public static <T> Either<Exception, T> tryCatch(CheckedFunction<T> function) {
		try {
			return Either.right(function.apply());
		} catch (Exception e) {
			return Either.left(e);
		}
	}

	public static <T> Either<Exception, T> tryCatch(CheckedSupplier<T> supplier) {
		try {
			return Either.right(supplier.get());
		} catch (Exception e) {
			return Either.left(e);
		}
	}

	public boolean isLeft() {
		return left.isPresent();
	}

	public L getLeft() {
		return left.get();
	}

	public R getRight() {
		return right.get();
	}

	public void either(Consumer<L> leftConsumer, Consumer<R> rightConsumer) {
		if (isLeft()) {
			leftConsumer.accept(getLeft());
		} else {
			rightConsumer.accept(getRight());
		}
	}

	public <T> T either(Function<L, T> leftFunction, Function<R, T> rightFunction) {
		if (isLeft()) {
			return leftFunction.apply(getLeft());
		} else {
			return rightFunction.apply(getRight());
		}
	}

	public <T> Either<L, T> map(Function<R, T> mapper) {
		if (isLeft()) {
			return Either.left(getLeft());
		} else {
			return Either.right(mapper.apply(getRight()));
		}
	}

	@FunctionalInterface
	public interface CheckedRunnable {
		void run() throws Exception;
	}

	@FunctionalInterface
	public interface CheckedSupplier<T> {
		T get() throws Exception;
	}

	@FunctionalInterface
	public interface CheckedFunction<T> {
		T apply() throws Exception;
	}

}
