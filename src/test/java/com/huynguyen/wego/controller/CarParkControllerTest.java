package com.huynguyen.wego.controller;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.huynguyen.wego.service.CarParkService;
import com.huynguyen.wego.service.ParkAvailabilityService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CarParkControllerTest {

    public static final DockerImageName MOCKSERVER_IMAGE = DockerImageName
        .parse("mockserver/mockserver");
    @Autowired
    private ParkAvailabilityService availabilityService;

    @Container
    static MockServerContainer mockServer = new MockServerContainer(MOCKSERVER_IMAGE);

    static MockServerClient mockServerClient;
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.24"));

    @DynamicPropertySource
    static void dynamicProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
            () -> "jdbc:mysql://localhost:" + mysql.getMappedPort(3306) + "/" + mysql.getDatabaseName());
        registry.add("com.huynguyen.wego.import.enabled", () -> "true");
        registry.add("com.huynguyen.wego.availability.sync.expression", () -> "-");
        registry.add("spring.datasource.username", () -> mysql.getUsername());
        registry.add("spring.datasource.password", () -> mysql.getPassword());
        registry.add(" com.huynguyen.wego.open-map.endpoint",
            () -> "http://" + mockServer.getHost() + ":" + mockServer.getServerPort());
        registry.add(" com.huynguyen.wego.availability.endpoint",
            () -> "http://" + mockServer.getHost() + ":" + mockServer.getServerPort());

    }

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        mockServerClient =
            new MockServerClient(
                mockServer.getHost(),
                mockServer.getServerPort()
            );
        mockServerClient
            .when(request().withMethod("GET")
                .withPath("^/commonapi/convert/3414to4326.*$"))
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                    .withBody(
                        json(
                            """
                                {
                                    "latitude":1.319729571666614,
                                    "longitude":103.84215609236949
                                }
                                """
                        )
                    )
            );

        mockServerClient
            .when(request()
                .withMethod("GET")
                .withPath("/v1/transport/carpark-availability"))
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                    .withBody(
                        json(
                            """
                                {
                                	"items": [
                                		{
                                			"timestamp": "2023-09-05T13:36:27+08:00",
                                			"carpark_data": [
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "105",
                                							"lot_type": "C",
                                							"lots_available": "36"
                                						}
                                					],
                                					"carpark_number": "ACB",
                                					"update_datetime": "2023-09-05T13:35:20"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "583",
                                							"lot_type": "C",
                                							"lots_available": "0"
                                						}
                                					],
                                					"carpark_number": "ACM",
                                					"update_datetime": "2023-09-05T13:35:57"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "329",
                                							"lot_type": "C",
                                							"lots_available": "119"
                                						}
                                					],
                                					"carpark_number": "AH1",
                                					"update_datetime": "2023-09-05T13:35:20"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "97",
                                							"lot_type": "C",
                                							"lots_available": "0"
                                						}
                                					],
                                					"carpark_number": "AK19",
                                					"update_datetime": "2023-09-05T13:35:51"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "96",
                                							"lot_type": "C",
                                							"lots_available": "0"
                                						}
                                					],
                                					"carpark_number": "Q81",
                                					"update_datetime": "2023-09-05T13:35:48"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "176",
                                							"lot_type": "C",
                                							"lots_available": "9"
                                						}
                                					],
                                					"carpark_number": "C20",
                                					"update_datetime": "2023-09-05T13:35:53"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "228",
                                							"lot_type": "C",
                                							"lots_available": "163"
                                						}
                                					],
                                					"carpark_number": "FR3M",
                                					"update_datetime": "2023-09-05T13:35:56"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "289",
                                							"lot_type": "C",
                                							"lots_available": "219"
                                						}
                                					],
                                					"carpark_number": "C32",
                                					"update_datetime": "2023-09-05T13:35:44"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "332",
                                							"lot_type": "C",
                                							"lots_available": "160"
                                						}
                                					],
                                					"carpark_number": "C6",
                                					"update_datetime": "2023-09-05T10:00:25"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "273",
                                							"lot_type": "C",
                                							"lots_available": "124"
                                						}
                                					],
                                					"carpark_number": "TG2",
                                					"update_datetime": "2023-09-05T13:35:21"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "577",
                                							"lot_type": "C",
                                							"lots_available": "388"
                                						}
                                					],
                                					"carpark_number": "BP1",
                                					"update_datetime": "2023-09-05T13:35:41"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "133",
                                							"lot_type": "C",
                                							"lots_available": "109"
                                						}
                                					],
                                					"carpark_number": "TG1",
                                					"update_datetime": "2023-09-05T13:35:39"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "189",
                                							"lot_type": "C",
                                							"lots_available": "129"
                                						}
                                					],
                                					"carpark_number": "TGM2",
                                					"update_datetime": "2023-09-05T13:35:21"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "134",
                                							"lot_type": "C",
                                							"lots_available": "65"
                                						}
                                					],
                                					"carpark_number": "TE14",
                                					"update_datetime": "2023-09-05T13:35:20"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "100",
                                							"lot_type": "C",
                                							"lots_available": "0"
                                						}
                                					],
                                					"carpark_number": "BM3",
                                					"update_datetime": "2023-09-05T13:35:26"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "612",
                                							"lot_type": "C",
                                							"lots_available": "121"
                                						}
                                					],
                                					"carpark_number": "BM9",
                                					"update_datetime": "2018-12-17T07:42:34"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "155",
                                							"lot_type": "C",
                                							"lots_available": "0"
                                						}
                                					],
                                					"carpark_number": "HG44",
                                					"update_datetime": "2023-09-05T13:35:57"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "82",
                                							"lot_type": "C",
                                							"lots_available": "10"
                                						}
                                					],
                                					"carpark_number": "HG64",
                                					"update_datetime": "2023-09-05T13:35:27"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "326",
                                							"lot_type": "C",
                                							"lots_available": "214"
                                						}
                                					],
                                					"carpark_number": "PM27",
                                					"update_datetime": "2023-09-05T13:32:58"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "305",
                                							"lot_type": "C",
                                							"lots_available": "192"
                                						}
                                					],
                                					"carpark_number": "PM28",
                                					"update_datetime": "2023-09-05T13:35:46"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "225",
                                							"lot_type": "C",
                                							"lots_available": "144"
                                						}
                                					],
                                					"carpark_number": "TM36",
                                					"update_datetime": "2023-09-05T13:35:11"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "244",
                                							"lot_type": "C",
                                							"lots_available": "178"
                                						}
                                					],
                                					"carpark_number": "TM37",
                                					"update_datetime": "2023-09-05T13:33:41"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "350",
                                							"lot_type": "C",
                                							"lots_available": "209"
                                						}
                                					],
                                					"carpark_number": "T50",
                                					"update_datetime": "2023-09-05T13:35:53"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "346",
                                							"lot_type": "C",
                                							"lots_available": "193"
                                						}
                                					],
                                					"carpark_number": "T51",
                                					"update_datetime": "2023-09-05T13:35:23"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "247",
                                							"lot_type": "C",
                                							"lots_available": "173"
                                						}
                                					],
                                					"carpark_number": "TM43",
                                					"update_datetime": "2023-09-05T13:35:53"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "30",
                                							"lot_type": "C",
                                							"lots_available": "0"
                                						}
                                					],
                                					"carpark_number": "T15",
                                					"update_datetime": "2023-09-05T13:35:53"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "109",
                                							"lot_type": "C",
                                							"lots_available": "65"
                                						}
                                					],
                                					"carpark_number": "T16",
                                					"update_datetime": "2023-09-05T13:35:51"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "228",
                                							"lot_type": "C",
                                							"lots_available": "103"
                                						}
                                					],
                                					"carpark_number": "T17",
                                					"update_datetime": "2023-09-05T13:35:46"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "81",
                                							"lot_type": "C",
                                							"lots_available": "0"
                                						}
                                					],
                                					"carpark_number": "T18",
                                					"update_datetime": "2023-09-05T13:35:59"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "213",
                                							"lot_type": "C",
                                							"lots_available": "166"
                                						}
                                					],
                                					"carpark_number": "B9",
                                					"update_datetime": "2023-09-05T13:35:14"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "516",
                                							"lot_type": "C",
                                							"lots_available": "299"
                                						}
                                					],
                                					"carpark_number": "B10",
                                					"update_datetime": "2023-09-05T13:35:34"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "208",
                                							"lot_type": "C",
                                							"lots_available": "61"
                                						}
                                					],
                                					"carpark_number": "B14",
                                					"update_datetime": "2018-10-29T09:18:49"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "143",
                                							"lot_type": "C",
                                							"lots_available": "3"
                                						}
                                					],
                                					"carpark_number": "WCB",
                                					"update_datetime": "2023-09-05T13:34:59"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "93",
                                							"lot_type": "C",
                                							"lots_available": "13"
                                						}
                                					],
                                					"carpark_number": "ACB",
                                					"update_datetime": "2023-09-05T13:35:15"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "28",
                                							"lot_type": "C",
                                							"lots_available": "0"
                                						}
                                					],
                                					"carpark_number": "CY",
                                					"update_datetime": "2023-09-05T13:35:17"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "403",
                                							"lot_type": "C",
                                							"lots_available": "224"
                                						}
                                					],
                                					"carpark_number": "AM46",
                                					"update_datetime": "2023-09-05T13:33:19"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "194",
                                							"lot_type": "C",
                                							"lots_available": "149"
                                						}
                                					],
                                					"carpark_number": "A12",
                                					"update_datetime": "2023-09-05T13:36:09"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "363",
                                							"lot_type": "C",
                                							"lots_available": "227"
                                						}
                                					],
                                					"carpark_number": "BE45",
                                					"update_datetime": "2023-09-05T13:36:09"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "705",
                                							"lot_type": "C",
                                							"lots_available": "383"
                                						}
                                					],
                                					"carpark_number": "BE39",
                                					"update_datetime": "2023-09-05T13:35:41"
                                				},
                                				{
                                					"carpark_info": [
                                						{
                                							"total_lots": "339",
                                							"lot_type": "C",
                                							"lots_available": "201"
                                						}
                                					],
                                					"carpark_number": "BE40",
                                					"update_datetime": "2019-06-18T10:58:35"
                                				}
                                			]
                                		}
                                	]
                                }
                                """
                        )
                    )
            );

    }

    @Test
    void findLocationsNearby() throws Exception {
        availabilityService.sync();
        
        mockMvc.perform(
                MockMvcRequestBuilders.get("/carparks/nearest?latitude=1.3197296&longitude=103.8421561&page=1&per_page=3"))
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().json(
                "[{\"address\":\"BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK\",\"latitude\":1.3197296,\"longitude\":103.8421561,\"total_lots\":93,\"available_lots\":13},{\"address\":\"BLK 101 JALAN DUSUN\",\"latitude\":1.3197296,\"longitude\":103.8421561,\"total_lots\":329,\"available_lots\":119}]"));

    }
}