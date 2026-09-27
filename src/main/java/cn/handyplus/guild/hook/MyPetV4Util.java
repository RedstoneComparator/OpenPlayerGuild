package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.entity.Pet;
import de.Keyle.MyPet.api.repository.PetManager;
import java.util.logging.Level;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

public class MyPetV4Util {
    private MyPetV4Util() {
    }

    public static void removeMyPet(Player player) {
        try {
            PetManager petManager = (PetManager)MyPetApi.class.getMethod("getPetManager", new Class[0]).invoke(null, new Object[0]);
            Pet pet = petManager.getPet(player);
            if (pet != null) {
                Mob bukkitEntity = pet.getBukkitEntity();
                if (bukkitEntity == null) {
                    pet.setStatus(Pet.PetState.Despawned);
                } else {
                    EntitySchedulerUtil.runSafeOnEntityScheduler((LivingEntity)bukkitEntity, () -> pet.setStatus(Pet.PetState.Despawned));
                }
            }
        }
        catch (ReflectiveOperationException exception) {
            PlayerGuild.INSTANCE.getLogger().log(Level.WARNING, "\u6536\u56deMyPet 4\u5ba0\u7269\u5931\u8d25", exception);
        }
    }
}
