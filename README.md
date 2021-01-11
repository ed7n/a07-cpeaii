# C Pea II

—Space shooter.

## Downloads

* [[**Latest Release**](https://github.com/ed7n/a07-cpeaii/raw/master/release/CPeaII.jar)] — Development C, 11/11/2020.

##### Supplements

* [[**Music by Chris Huelsbeck**](https://drive.google.com/file/d/1_OzTJVDz5zZSV9RG0cWQ2ZC4mD3B3U9h/view)] — from *Mega Turrican*, 19.2 MiB.
  * Optional. Put it alongside the JAR file.

##### Screenshots
* [[**Latest Release**](https://github.com/ed7n/a07-cpeaii/raw/master/release/Screenshot.png)] — on Debian GNU/Linux.

## Building

    $ javac -d release --release 8 --source-path src src/eden/cpeaii/GameFrame.java && jar -c -f release/CPeaII.jar -e eden.cpeaii.GameFrame -C release eden -C res FIRE.WAV

## About

This is a working developmental version of a (slowly-upcoming) project,
code-named C Pea II. It demonstrates some basic game mechanics such as moving
(with the arrow keys), firing (with the `X` key) and dying. It emerged from an
assignment to a course taught by Jeremy Hilliker, who in turn helped with some
parts of the project.

### Enhancements

*This section is related to the original assignment, in which enhancements had
to be mentioned in order to be noticed and be awarded bonus marks. The
assignment came provided with a skeleton to build upon.*

Most additional `.java` files are my enhancements, notably:

* **`BlasterFactory`** — [x] to fire a `Blaster` out of the `VicViper`.
* **`LineParticleFactory`** — Disassembles a Sprite into `LineParticles`.
* **`StarDust`** — A stardust.
* **`StarDustTile`** — a collection of `StarDusts` acting as a background tile.

Otherwise:

* Diagonal movement.
* "Realistic" asteroids.
* Game pauses upon window focus lost.
* Optional music by Chris Huelsbeck from *Mega Turrican* [Genesis, 1994].

### Future Plans

I have actual plans to expand this into an entire game--hopefully (11/11/2020:
it's been over three years now). This is why you may see:

* Unused methods.
* Partial Javadoc.
* Empty interfaces.
* Non-sense inheritance.
* Unused enums, like `Effect`.
* One liner methods, like `update()` in `VectorSprite`.

Instead of aiming for program-specific functionality, my goal is to have
everything reusable to other projects if I happen to need any of them.

Some planned features:

* `Sprite`-to-`Sprite` interaction.
* Game scenes/levels/stages/worlds (each as component class?).
* Rotation and scaling.
* Smooth animation.
* Motion blur.*
* [[**Pseudo-3D**](https://en.wikipedia.org/wiki/Mode_7)].*

*not sure.

### Class Model

* Everything on screen are `Sprites`, similar to Neo Geo AVS arcade machines.
* A `Sprite` may be vector or raster (confusing, may be unified to a class).
* A `Sprite` may be foreground (`VicViper`, `Asteroid`) or background (`StarDust`).
* A `SpriteFactory` makes `Sprites`.
* A `SpriteFactory` can either be attachable (`AsteroidFactory`, `BlasterFactory`), or ??? (`LineParticleFactory`).

## Credits

* Jeremy Hilliker
  * Program skeleton.
  * OO Programming instructor.
  * Small chocolate distributor.
