/*******************************************************************************************************
 * Version 1.0 authored by FlyingPikachu/HappyPikachu. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************************************/

package me.happypikachu.CustomMobsQuests;

import de.hellfirepvp.api.event.CustomMobDeathEvent;
import java.util.Map;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CustomMobsKillObjective extends CustomObjective implements Listener {
  private static Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
  
  public CustomMobsKillObjective() {
    setName("Kill CustomMobs Objective");
    setAuthor("HappyPikachu");
    setEnableCount(true);
    setShowCount(true);
    addData("objName");
    addDescription("objName", "Set a name for the objective");
    addData("killNames");
    addDescription("killNames", "Enter CustomMobs names, separating each one by a comma");
    setCountPrompt("Set the amount of CustomMobs to kill");
    setDisplay("Kill %objName%: %count%");
  }
  
  @EventHandler
  public void on(CustomMobDeathEvent event) {
    Player killer = event.getKiller();
    Quester quester = quests.getQuester(killer.getUniqueId());
    if (quester == null) {
      return;
    }
    String mobName = event.getMob().getName();
    for (Quest q : quester.currentQuests.keySet()) {
      Map<String, Object> datamap = getDatamap(killer, this, q);
      if (datamap != null) {
        String mobNames = (String)datamap.getOrDefault("killNames", null);
        if (mobNames == null) {
          return;
        }
        String[] spl = mobNames.split(",");
        for (String str : spl) {
          if ((str != null) && (mobName.equalsIgnoreCase(str))) {
            incrementObjective(killer, this, 1, q);
            return;
          }
        }
      }
    }
  }
}