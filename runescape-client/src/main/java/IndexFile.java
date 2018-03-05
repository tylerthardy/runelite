import java.io.EOFException;
import java.io.IOException;
import net.runelite.mapping.Export;
import net.runelite.mapping.Implements;
import net.runelite.mapping.ObfuscatedGetter;
import net.runelite.mapping.ObfuscatedName;
import net.runelite.mapping.ObfuscatedSignature;

@ObfuscatedName("fu")
@Implements("IndexFile")
public final class IndexFile {
   @ObfuscatedName("ni")
   @ObfuscatedSignature(
      signature = "Lkl;"
   )
   static class301 field2185;
   @ObfuscatedName("s")
   @Export("IndexStore_buffer")
   static byte[] IndexStore_buffer;
   @ObfuscatedName("z")
   @ObfuscatedGetter(
      intValue = -1307186807
   )
   public static int field2192;
   @ObfuscatedName("v")
   @ObfuscatedGetter(
      intValue = 2045136949
   )
   @Export("canvasHeight")
   protected static int canvasHeight;
   @ObfuscatedName("bv")
   @Export("sessionToken")
   static String sessionToken;
   @ObfuscatedName("g")
   @ObfuscatedSignature(
      signature = "Lcy;"
   )
   @Export("dataFile")
   CacheFile dataFile;
   @ObfuscatedName("m")
   @ObfuscatedSignature(
      signature = "Lcy;"
   )
   @Export("indexFile")
   CacheFile indexFile;
   @ObfuscatedName("h")
   @ObfuscatedGetter(
      intValue = 1580633103
   )
   @Export("index")
   int index;
   @ObfuscatedName("i")
   @ObfuscatedGetter(
      intValue = 1166502847
   )
   @Export("maxSize")
   int maxSize;

   static {
      IndexStore_buffer = new byte[520];
   }

   @ObfuscatedSignature(
      signature = "(ILcy;Lcy;I)V"
   )
   public IndexFile(int var1, CacheFile var2, CacheFile var3, int var4) {
      this.dataFile = null;
      this.indexFile = null;
      this.maxSize = 65000;
      this.index = var1;
      this.dataFile = var2;
      this.indexFile = var3;
      this.maxSize = var4;
   }

   @ObfuscatedName("s")
   @ObfuscatedSignature(
      signature = "(II)[B",
      garbageValue = "-774740927"
   )
   @Export("read")
   public byte[] read(int var1) {
      CacheFile var2 = this.dataFile;
      synchronized(this.dataFile) {
         try {
            Object var10000;
            if(this.indexFile.length() < (long)(var1 * 6 + 6)) {
               var10000 = null;
               return (byte[])var10000;
            } else {
               this.indexFile.seek((long)(var1 * 6));
               this.indexFile.read(IndexStore_buffer, 0, 6);
               int var3 = ((IndexStore_buffer[0] & 255) << 16) + (IndexStore_buffer[2] & 255) + ((IndexStore_buffer[1] & 255) << 8);
               int var4 = (IndexStore_buffer[5] & 255) + ((IndexStore_buffer[3] & 255) << 16) + ((IndexStore_buffer[4] & 255) << 8);
               if(var3 < 0 || var3 > this.maxSize) {
                  var10000 = null;
                  return (byte[])var10000;
               } else if(var4 <= 0 || (long)var4 > this.dataFile.length() / 520L) {
                  var10000 = null;
                  return (byte[])var10000;
               } else {
                  byte[] var5 = new byte[var3];
                  int var6 = 0;
                  int var7 = 0;

                  while(var6 < var3) {
                     if(var4 == 0) {
                        var10000 = null;
                        return (byte[])var10000;
                     }

                     this.dataFile.seek((long)(var4 * 520));
                     int var8 = var3 - var6;
                     if(var8 > 512) {
                        var8 = 512;
                     }

                     this.dataFile.read(IndexStore_buffer, 0, var8 + 8);
                     int var9 = (IndexStore_buffer[1] & 255) + ((IndexStore_buffer[0] & 255) << 8);
                     int var10 = (IndexStore_buffer[3] & 255) + ((IndexStore_buffer[2] & 255) << 8);
                     int var11 = ((IndexStore_buffer[5] & 255) << 8) + ((IndexStore_buffer[4] & 255) << 16) + (IndexStore_buffer[6] & 255);
                     int var12 = IndexStore_buffer[7] & 255;
                     if(var9 == var1 && var7 == var10 && var12 == this.index) {
                        if(var11 >= 0 && (long)var11 <= this.dataFile.length() / 520L) {
                           for(int var13 = 0; var13 < var8; ++var13) {
                              var5[var6++] = IndexStore_buffer[var13 + 8];
                           }

                           var4 = var11;
                           ++var7;
                           continue;
                        }

                        var10000 = null;
                        return (byte[])var10000;
                     }

                     var10000 = null;
                     return (byte[])var10000;
                  }

                  byte[] var18 = var5;
                  return var18;
               }
            }
         } catch (IOException var16) {
            return null;
         }
      }
   }

   @ObfuscatedName("g")
   @ObfuscatedSignature(
      signature = "(I[BIB)Z",
      garbageValue = "-28"
   )
   @Export("write")
   public boolean write(int var1, byte[] var2, int var3) {
      CacheFile var4 = this.dataFile;
      synchronized(this.dataFile) {
         if(var3 >= 0 && var3 <= this.maxSize) {
            boolean var5 = this.write0(var1, var2, var3, true);
            if(!var5) {
               var5 = this.write0(var1, var2, var3, false);
            }

            return var5;
         } else {
            throw new IllegalArgumentException();
         }
      }
   }

   @ObfuscatedName("m")
   @ObfuscatedSignature(
      signature = "(I[BIZI)Z",
      garbageValue = "933004585"
   )
   @Export("write0")
   boolean write0(int var1, byte[] var2, int var3, boolean var4) {
      CacheFile var5 = this.dataFile;
      synchronized(this.dataFile) {
         try {
            int var6;
            boolean var10000;
            if(var4) {
               if(this.indexFile.length() < (long)(var1 * 6 + 6)) {
                  var10000 = false;
                  return var10000;
               }

               this.indexFile.seek((long)(var1 * 6));
               this.indexFile.read(IndexStore_buffer, 0, 6);
               var6 = (IndexStore_buffer[5] & 255) + ((IndexStore_buffer[3] & 255) << 16) + ((IndexStore_buffer[4] & 255) << 8);
               if(var6 <= 0 || (long)var6 > this.dataFile.length() / 520L) {
                  var10000 = false;
                  return var10000;
               }
            } else {
               var6 = (int)((this.dataFile.length() + 519L) / 520L);
               if(var6 == 0) {
                  var6 = 1;
               }
            }

            IndexStore_buffer[0] = (byte)(var3 >> 16);
            IndexStore_buffer[1] = (byte)(var3 >> 8);
            IndexStore_buffer[2] = (byte)var3;
            IndexStore_buffer[3] = (byte)(var6 >> 16);
            IndexStore_buffer[4] = (byte)(var6 >> 8);
            IndexStore_buffer[5] = (byte)var6;
            this.indexFile.seek((long)(var1 * 6));
            this.indexFile.write(IndexStore_buffer, 0, 6);
            int var7 = 0;
            int var8 = 0;

            while(true) {
               if(var7 < var3) {
                  label143: {
                     int var9 = 0;
                     int var14;
                     if(var4) {
                        this.dataFile.seek((long)(var6 * 520));

                        try {
                           this.dataFile.read(IndexStore_buffer, 0, 8);
                        } catch (EOFException var16) {
                           break label143;
                        }

                        var14 = (IndexStore_buffer[1] & 255) + ((IndexStore_buffer[0] & 255) << 8);
                        int var11 = (IndexStore_buffer[3] & 255) + ((IndexStore_buffer[2] & 255) << 8);
                        var9 = ((IndexStore_buffer[5] & 255) << 8) + ((IndexStore_buffer[4] & 255) << 16) + (IndexStore_buffer[6] & 255);
                        int var12 = IndexStore_buffer[7] & 255;
                        if(var14 != var1 || var11 != var8 || var12 != this.index) {
                           var10000 = false;
                           return var10000;
                        }

                        if(var9 < 0 || (long)var9 > this.dataFile.length() / 520L) {
                           var10000 = false;
                           return var10000;
                        }
                     }

                     if(var9 == 0) {
                        var4 = false;
                        var9 = (int)((this.dataFile.length() + 519L) / 520L);
                        if(var9 == 0) {
                           ++var9;
                        }

                        if(var9 == var6) {
                           ++var9;
                        }
                     }

                     if(var3 - var7 <= 512) {
                        var9 = 0;
                     }

                     IndexStore_buffer[0] = (byte)(var1 >> 8);
                     IndexStore_buffer[1] = (byte)var1;
                     IndexStore_buffer[2] = (byte)(var8 >> 8);
                     IndexStore_buffer[3] = (byte)var8;
                     IndexStore_buffer[4] = (byte)(var9 >> 16);
                     IndexStore_buffer[5] = (byte)(var9 >> 8);
                     IndexStore_buffer[6] = (byte)var9;
                     IndexStore_buffer[7] = (byte)this.index;
                     this.dataFile.seek((long)(var6 * 520));
                     this.dataFile.write(IndexStore_buffer, 0, 8);
                     var14 = var3 - var7;
                     if(var14 > 512) {
                        var14 = 512;
                     }

                     this.dataFile.write(var2, var7, var14);
                     var7 += var14;
                     var6 = var9;
                     ++var8;
                     continue;
                  }
               }

               var10000 = true;
               return var10000;
            }
         } catch (IOException var17) {
            return false;
         }
      }
   }

   @ObfuscatedName("s")
   @ObfuscatedSignature(
      signature = "(II)Ljp;",
      garbageValue = "-1913986945"
   )
   public static Varbit method3336(int var0) {
      Varbit var1 = (Varbit)Varbit.varbits.get((long)var0);
      if(var1 != null) {
         return var1;
      } else {
         byte[] var2 = Varbit.varbit_ref.getConfigData(14, var0);
         var1 = new Varbit();
         if(var2 != null) {
            var1.decode(new Buffer(var2));
         }

         Varbit.varbits.put(var1, (long)var0);
         return var1;
      }
   }

   @ObfuscatedName("s")
   @ObfuscatedSignature(
      signature = "(ILhj;I)I",
      garbageValue = "-1091030470"
   )
   public static int method3329(int var0, class230 var1) {
      return (var0 << 8) + var1.field2899;
   }

   @ObfuscatedName("m")
   @ObfuscatedSignature(
      signature = "(I)I",
      garbageValue = "1921783371"
   )
   public static int method3332() {
      return ++MouseInput.mouseIdleTicks - 1;
   }
}
