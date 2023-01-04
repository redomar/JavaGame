package com.redomar.game.lib;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Either<L, R> {
	private final Optional<L> left;
	private final Optional<R> right;

	private Either(final Optional<L> left, final Optional<R> right) {
		this.left = left;
		this.right = right;
	}

	@Contract("_ -> new")
	static <L, R> @NotNull Either<L, R> left(final L value) {
		return new Either<>(Optional.of(value), Optional.empty());
	}

	@Contract("_ -> new")
	static <L, R> @NotNull Either<L, R> right(final R value) {
		return new Either<>(Optional.empty(), Optional.of(value));
	}

	boolean isLeft() {
		return left.isPresent();
	}

	L getLeft() {
		return left.get();
	}

	R getRight() {
		return right.get();
	}
}
