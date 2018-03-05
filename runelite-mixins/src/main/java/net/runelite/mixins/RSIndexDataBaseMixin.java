/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.mixins;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.runelite.api.mixins.Copy;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.api.overlay.OverlayIndex;
import static net.runelite.client.callback.Hooks.log;
import net.runelite.rs.api.RSIndexData;
import net.runelite.rs.api.RSIndexDataBase;

@Mixin(RSIndexDataBase.class)
public abstract class RSIndexDataBaseMixin implements RSIndexDataBase
{
	@Copy("getConfigData")
	abstract byte[] rs$getConfigData(int archiveId, int fileId);

	@Replace("getConfigData")
	public byte[] rl$getConfigData(int archiveId, int fileId)
	{
		byte[] rsData = rs$getConfigData(archiveId, fileId);
		RSIndexData indexData = (RSIndexData) this;

		if (!OverlayIndex.hasOverlay(indexData.getIndex(), archiveId))
		{
			return rsData;
		}

		InputStream in = getClass().getResourceAsStream("/runelite/" + indexData.getIndex() + "/" + archiveId);
		if (in == null)
		{
			log.warn("Missing overlay data for {}/{}", indexData.getIndex(), archiveId);
			return rsData;
		}

		HashCode rsDataHash = Hashing.sha256().hashBytes(rsData);

		String rsHash = BaseEncoding.base16().encode(rsDataHash.asBytes());

		InputStream in2 = getClass().getResourceAsStream("/runelite/" + indexData.getIndex() + "/" + archiveId + ".hash");
		if (in2 == null)
		{
			log.warn("Missing hash file for {}/{}", indexData.getIndex(), archiveId);
			return rsData;
		}

		try
		{
			String replaceHash = CharStreams.toString(new InputStreamReader(in2));

			if (replaceHash.equals(rsHash))
			{
				log.debug("Replacing archive {}/{}",
					indexData.getIndex(), archiveId);
				return ByteStreams.toByteArray(in);
			}

			log.warn("Mismatch in overlaid cache archive hash for {}/{}: {} != {}",
				indexData.getIndex(), archiveId, replaceHash, rsHash);
		}
		catch (IOException ex)
		{
			log.warn("error checking hash", ex);
		}

		return rsData;
	}
}
