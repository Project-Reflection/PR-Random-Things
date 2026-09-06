package prrandomthings.api.exception;

import net.minecraft.util.math.BlockPos;

public class ChunkSupressionException extends RuntimeException {
    public ChunkSupressionException(BlockPos pos){
        super("Chunk suppression at:"+pos.toString());
    }
    public ChunkSupressionException(String message) {
        super(message);
    }
}
