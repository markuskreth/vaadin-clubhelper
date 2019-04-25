package de.kreth.vaadin.clubhelper;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("de.kreth.vaadin.clubhelper")
@ExcludeTags("spring")
public class FastTestsOnly {
}
