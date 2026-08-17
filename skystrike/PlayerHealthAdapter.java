package skystrike;

public class PlayerHealthAdapter implements Damageable {

    @Override
    public void takeDamage(int amount) {
        GameManager.getInstance().damagePlayer(amount);
    }

    @Override
    public boolean isDestroyed() {
        return GameManager.getInstance().isPlayerDead();
    }
}
