package com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.config;

import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.config.SulClientConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.config.SulClientConfigStandard;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.config.SulServerConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.config.SulServerConfigStandard;
import com.github.protocolfuzzing.protocolstatefuzzer.entrypoints.CommandLineParser;
import com.github.protocolfuzzing.protocolstatefuzzer.entrypoints.CommandLineParserTest;
import com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.core.config.StateFuzzerClientConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.core.config.StateFuzzerClientConfigStandard;
import com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.core.config.StateFuzzerConfigBuilder;
import com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.core.config.StateFuzzerServerConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.core.config.StateFuzzerServerConfigStandard;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MapperConfigTest<M> {
    public static String[] SUL_CLIENT_CONFIG_STANDARD_REQ_ARGS = new String[] { "-port", "1234" };
    public static String[] SUL_SERVER_CONFIG_STANDARD_REQ_ARGS = new String[] { "-connect", "host:1234" };

    @Test
    public void parseAllOptions_SFCstd_SULCstd_SFSstd_SULSstd() {
        parseAllOptions(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfigStandard(null, new SulClientConfigStandard(new MapperConfigStandard(), null), null, null);
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfigStandard(null, new SulServerConfigStandard(new MapperConfigStandard(), null), null, null);
                }
            },
            SUL_CLIENT_CONFIG_STANDARD_REQ_ARGS,
            SUL_SERVER_CONFIG_STANDARD_REQ_ARGS
        );
    }

    private void parseAllOptions(StateFuzzerConfigBuilder stateFuzzerConfigBuilder, String[] clientReqArgs, String[] serverReqArgs) {
        String mapperConnectionConfig = "mapperConnectionConfigFile";
        List<String> repeatingOutputs = List.of("OUT_1", "OUT_2");

        String[] partialArgs = new String[] {
            "-mapperConnectionConfig", mapperConnectionConfig,
            "-repeatingOutputs", String.join(",", repeatingOutputs),
            "-socketClosedAsTimeout",
            "-disabledAsTimeout",
            "-dontMergeRepeating"
        };

        MapperConfig[] mapperConfigs = parseWithStandard(stateFuzzerConfigBuilder, partialArgs, clientReqArgs, serverReqArgs);

        for (MapperConfig mapperConfig : mapperConfigs) {
            Assert.assertNotNull(mapperConfig);
            Assert.assertEquals(mapperConnectionConfig, mapperConfig.getMapperConnectionConfig());
            Assert.assertEquals(repeatingOutputs, mapperConfig.getRepeatingOutputs());
            Assert.assertTrue(mapperConfig.isSocketClosedAsTimeout());
            Assert.assertTrue(mapperConfig.isDisabledAsTimeout());
            Assert.assertFalse(mapperConfig.isMergeRepeating());
        }
    }

    private MapperConfig[] parseWithStandard(StateFuzzerConfigBuilder stateFuzzerConfigBuilder,
        String[] partialArgs, String[] clientReqArgs, String[] serverReqArgs) {

        CommandLineParser<M> commandLineParser = new CommandLineParser<>(stateFuzzerConfigBuilder, null, null, null);

        MapperConfig[] mapperConfigs = new MapperConfig[2];

        clientReqArgs = clientReqArgs == null ? new String[0] : clientReqArgs;
        String[] clientPartialArgs = CommandLineParserTest.concatArgs(partialArgs, clientReqArgs);
        StateFuzzerClientConfig clientConfig = CommandLineParserTest.parseClientArgs(commandLineParser, clientPartialArgs);
        Assert.assertNotNull(clientConfig);
        Assert.assertNotNull(clientConfig.getSulConfig());
        mapperConfigs[0] = clientConfig.getSulConfig().getMapperConfig();

        serverReqArgs = serverReqArgs == null ? new String[0] : serverReqArgs;
        String[] serverPartialArgs = CommandLineParserTest.concatArgs(partialArgs, serverReqArgs);
        StateFuzzerServerConfig serverConfig = CommandLineParserTest.parseServerArgs(commandLineParser, serverPartialArgs);
        Assert.assertNotNull(serverConfig);
        Assert.assertNotNull(serverConfig.getSulConfig());
        mapperConfigs[1] = serverConfig.getSulConfig().getMapperConfig();

        return mapperConfigs;
    }

    @Test
    public void invalidParseWithEmpty_SFCstd_SULCstd_SFSstd_SULSstd() {
        invalidParseWithEmpty(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfigStandard(null, new SulClientConfigStandard(new MapperConfig(){}, null), null, null);
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfigStandard(null, new SulServerConfigStandard(new MapperConfig(){}, null), null, null);
                }
            },
            SUL_CLIENT_CONFIG_STANDARD_REQ_ARGS,
            SUL_SERVER_CONFIG_STANDARD_REQ_ARGS
        );
    }

    @Test
    public void invalidParseWithEmpty_SFCstd_SULCstd_SFSstd_SULSemp() {
        invalidParseWithEmpty(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfigStandard(null, new SulClientConfigStandard(new MapperConfig(){}, null), null, null);
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfigStandard(null, new SulServerConfig(){}, null, null);
                }
            },
            SUL_CLIENT_CONFIG_STANDARD_REQ_ARGS,
            null
        );
    }

    @Test
    public void invalidParseWithEmpty_SFCstd_SULCstd_SFSemp() {
        invalidParseWithEmpty(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfigStandard(null, new SulClientConfigStandard(new MapperConfig(){}, null), null, null);
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfig(){};
                }
            },
            SUL_CLIENT_CONFIG_STANDARD_REQ_ARGS,
            SUL_SERVER_CONFIG_STANDARD_REQ_ARGS
        );
    }

    @Test
    public void invalidParseWithEmpty_SFCstd_SULCemp_SFSstd_SULSstd() {
        invalidParseWithEmpty(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfigStandard(null, new SulClientConfig(){}, null, null);
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfigStandard(null, new SulServerConfigStandard(new MapperConfig(){}, null), null, null);
                }
            },
            null,
            SUL_SERVER_CONFIG_STANDARD_REQ_ARGS
        );
    }

    @Test
    public void invalidParseWithEmpty_SFCemp_SFSstd_SULSstd() {
        invalidParseWithEmpty(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfig(){};
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfigStandard(null, new SulServerConfigStandard(new MapperConfig(){}, null), null, null);
                }
            },
            SUL_CLIENT_CONFIG_STANDARD_REQ_ARGS,
            SUL_SERVER_CONFIG_STANDARD_REQ_ARGS
        );
    }

    @Test
    public void invalidParseWithEmpty_SFCstd_SULCemp_SFSstd_SULSemp() {
        invalidParseWithEmpty(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfigStandard(null, new SulClientConfig(){}, null, null);
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfigStandard(null, new SulServerConfig(){}, null, null);
                }
            },
            null,
            null
        );
    }

    @Test
    public void invalidParseWithEmpty_SFCstd_SULCemp_SFSemp() {
        invalidParseWithEmpty(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfigStandard(null, new SulClientConfig(){}, null, null);
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfig(){};
                }
            },
            null,
            SUL_SERVER_CONFIG_STANDARD_REQ_ARGS
        );
    }

    @Test
    public void invalidParseWithEmpty_SFCemp_SFSemp() {
        invalidParseWithEmpty(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfig(){};
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfig(){};
                }
            },
            SUL_CLIENT_CONFIG_STANDARD_REQ_ARGS,
            SUL_SERVER_CONFIG_STANDARD_REQ_ARGS
        );
    }

    @Test
    public void invalidParseWithEmpty_SFCemp_SFSstd_SULSemp() {
        invalidParseWithEmpty(
            new StateFuzzerConfigBuilder() {
                @Override
                public StateFuzzerClientConfig buildClientConfig() {
                    return new StateFuzzerClientConfig(){};
                }
                @Override
                public StateFuzzerServerConfig buildServerConfig() {
                    return new StateFuzzerServerConfigStandard(null, new SulServerConfig(){}, null, null);
                }
            },
            null,
            null
        );
    }

    private void invalidParseWithEmpty(StateFuzzerConfigBuilder stateFuzzerConfigBuilder, String[] clientReqArgs, String[] serverReqArgs) {
        CommandLineParser<M> commandLineParser = new CommandLineParser<>(stateFuzzerConfigBuilder, null, null, null);

        String[] partialArgs = new String[] {
            "-mapperConnectionConfig", "mapperConnectionConfigPath",
        };

        clientReqArgs = clientReqArgs == null ? new String[0] : clientReqArgs;
        String[] clientPartialArgs = CommandLineParserTest.concatArgs(partialArgs, clientReqArgs);
        CommandLineParserTest.assertInvalidClientParse(commandLineParser, clientPartialArgs);

        serverReqArgs = serverReqArgs == null ? new String[0] : serverReqArgs;
        String[] serverPartialArgs = CommandLineParserTest.concatArgs(partialArgs, serverReqArgs);
        CommandLineParserTest.assertInvalidServerParse(commandLineParser, serverPartialArgs);
    }
}
