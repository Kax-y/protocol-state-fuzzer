package com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.config;

import com.beust.jcommander.Parameter;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.config.MapperConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.config.MapperConnectionConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.config.MapperConnectionConfigException;

import java.io.PrintWriter;

public abstract class SulServerConfig extends SulConfig {

    @Parameter(names = "-connect", required = true, description = "Address of server to connect the mapper. Format: ip:port")
    protected String host = null;

    public SulServerConfig() {
        super();
    }

    public SulServerConfig(MapperConfig mapperConfig) {
        super(mapperConfig);
    }

    public abstract void applyDelegate(MapperConnectionConfig config) throws MapperConnectionConfigException;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public final String getFuzzingRole() {
        return "server";
    }

    @Override
    public final boolean isFuzzingClient() {
        return false;
    }

    @Override
    public void printRunDescriptionSelf(PrintWriter printWriter) {
        super.printRunDescriptionSelf(printWriter);
        printWriter.println("SulServerConfig Parameters");
        printWriter.println("Connect to: " + getHost());
    }
}
