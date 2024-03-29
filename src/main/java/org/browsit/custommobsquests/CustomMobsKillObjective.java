/*
 * Copyright (c) 2018 PikaMug and contributors. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.browsit.custommobsquests;

import de.hellfirepvp.api.event.CustomMobDeathEvent;
import me.pikamug.quests.enums.ObjectiveType;
import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class CustomMobsKillObjective extends BukkitCustomObjective implements Listener {
    
    public CustomMobsKillObjective() {
        setName("CustomMobs Kill Mobs Objective");
        setAuthor("PikaMug");
		setItem("STONE_SWORD", (short)0);
        setShowCount(true);
        addStringPrompt("CM Kill Obj", "Set a name for the objective", "Kill custom mob");
        addStringPrompt("CM Kill Name", "Enter custom mob names, separating each one by a comma", "ANY");
        setCountPrompt("Set the amount of custom mobs to kill");
        setDisplay("%CM Obj Name% %CM Kill Name%: %count%");
    }
    
    @EventHandler
    public void onCustomMobDeath(final CustomMobDeathEvent event) {
		final Player killer = event.getKiller();
        final Quester quester = CustomMobsModule.getQuests().getQuester(killer.getUniqueId());
		if (quester == null) {
			return;
		}
        final String mobName = event.getMob().getName();
		for (final Quest quest : quester.getCurrentQuests().keySet()) {
			final Map<String, Object> datamap = getDataForPlayer(killer.getUniqueId(), this, quest);
			if (datamap != null) {
				final String mobNames = (String)datamap.getOrDefault("CM Kill Name", "ANY");
				if (mobNames == null) {
					return;
				}
				final String[] spl = mobNames.split(",");
				for (final String str : spl) {
					if (str.equals("ANY") || mobName.equalsIgnoreCase(str)) {
						incrementObjective(killer.getUniqueId(), this, quest, 1);

						quester.dispatchMultiplayerEverything(quest, ObjectiveType.CUSTOM,
								(final Quester q, final Quest cq) -> {
									incrementObjective(q.getUUID(), this, quest, 1);
									return null;
								});
						return;
					}
				}
			}
		}
	}
}
