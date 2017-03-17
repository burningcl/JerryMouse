package com.skyline.db.jerrymouse.generator;

/**
 * Created by chenliang on 2017/3/14.
 */

public class Test {

	public static void main(String... strings) {

		String targetClassPath = "/Users/chenliang/AndroidProjects/JerryMouse/core/build/intermediates/classes/androidTest/debug";
		String[] classpaths = {
				"/Users/chenliang/AndroidProjects/JerryMouse/core/build/intermediates/bundles/debug/classes.jar",
				"/Users/chenliang/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar",
				"/Users/chenliang/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar",
				"/Users/chenliang/.android/build-cache/eba3804da92c02855e7092d84f60e1ad53afd8c5/output/jars/classes.jar",
				"/Users/chenliang/.android/build-cache/1dd48323efdbdc9bffb78c090902ad49dca83092/output/jars/classes.jar",
				"/Users/chenliang/.android/build-cache/1dd48323efdbdc9bffb78c090902ad49dca83092/output/jars/libs/internal_impl-21.0.3.jar",
				"/Users/chenliang/Library/Android/sdk/extras/android/m2repository/com/android/support/support-annotations/21.0.3/support-annotations-21.0.3.jar",
				"/Users/chenliang/Library/Android/sdk/platforms/android-21/android.jar"
		};

		new ByteCodeMapperGenerator().gen(targetClassPath, classpaths);
	}

}
