package net.moonlightflower.wc3libs.dataTypes.app;

import net.moonlightflower.wc3libs.misc.Id;
import net.moonlightflower.wc3libs.misc.ObjId;

import javax.annotation.Nonnull;

public class UberSplatId extends ObjId {
	protected UberSplatId(String idString) {
		super(idString);
	}

	@Nonnull
	public static UberSplatId valueOf(Id val) {
		return new UberSplatId(val.toString());
	}
}
