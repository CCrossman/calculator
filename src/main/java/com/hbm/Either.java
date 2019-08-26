package com.hbm;

import java.util.function.Function;

public abstract class Either<A,B> {

	// sealed class
	private Either() {}

	public abstract <AA extends A, BB> Either<A,BB> flatMap(Function<B,Either<AA,BB>> fn);

	public abstract <X> X fold(Function<A,X> onLeft, Function<B,X> onRight);

	public static <A,B> Left<A,B> left(A value) {
		return new Left<>(value);
	}

	public static <A,B> Right<A,B> right(B value) {
		return new Right<>(value);
	}

	public static final class Left<A,B> extends Either<A,B> {
		private final A value;

		public Left(A value) {
			this.value = value;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <AA extends A, BB> Either<A, BB> flatMap(Function<B, Either<AA, BB>> fn) {
			return (Either<A, BB>) this;
		}

		@Override
		public <X> X fold(Function<A, X> onLeft, Function<B, X> onRight) {
			return onLeft.apply(value);
		}
	}

	public static final class Right<A,B> extends Either<A,B> {
		private final B value;

		public Right(B value) {
			this.value = value;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <AA extends A, BB> Either<A, BB> flatMap(Function<B, Either<AA, BB>> fn) {
			return (Either<A, BB>) fn.apply(value);
		}

		@Override
		public <X> X fold(Function<A, X> onLeft, Function<B, X> onRight) {
			return onRight.apply(value);
		}
	}
}
