package net.runelite.client.plugins.osleague.osleague;

import com.google.gson.annotations.SerializedName;
import net.runelite.client.plugins.osleague.Relic;

public class OsLeagueRelic
{
    public boolean passive = true;
    @SerializedName("relic")
    public int relicId;

    public OsLeagueRelic(Relic relic)
    {
        this.relicId = relic.getRelicId();
    }
}
