package ca.ulaval.glo4002.application.infrastructure;

import ca.ulaval.glo4002.application.application.AccessService;
import ca.ulaval.glo4002.application.application.BuildingStateService;
import ca.ulaval.glo4002.application.application.EventService;
import ca.ulaval.glo4002.application.application.ResetService;
import ca.ulaval.glo4002.application.domain.action.*;
import ca.ulaval.glo4002.application.domain.building.ActionContextFactory;
import ca.ulaval.glo4002.application.domain.repository.BuildingMapRepo;
import ca.ulaval.glo4002.application.domain.repository.CampusDirectoryRepo;
import ca.ulaval.glo4002.application.domain.repository.StatePersistenceRepo;
import ca.ulaval.glo4002.application.domain.ventilation.VentilationContextFactory;
import ca.ulaval.glo4002.application.domain.zone.gathering.GatheringFactory;
import ca.ulaval.glo4002.application.domain.zone.intervention.InterventionFactory;
import ca.ulaval.glo4002.application.infrastructure.external_services.building_map.*;
import ca.ulaval.glo4002.application.infrastructure.persistence.PersistenceFactory;
import ca.ulaval.glo4002.application.infrastructure.persistence.mappers.BuildingStateMapper;
import ca.ulaval.glo4002.application.infrastructure.persistence.mappers.DoorStateMapper;
import ca.ulaval.glo4002.application.infrastructure.persistence.mappers.RoomStateMapper;
import ca.ulaval.glo4002.application.infrastructure.persistence.mappers.ZoneStateMapper;
import ca.ulaval.glo4002.application.infrastructure.persistence.sqlite.*;

public class ApplicationFactory {
  private static StatePersistenceRepo sharedStateRepo;
  private static BuildingMapRepo sharedBuildingMapRepo;
  private static BuildingStateMapper buildingStateMapper;

  private static void initStateRepo() {
    VentilationContextFactory ventilationContextFactory = new VentilationContextFactory();
    DoorStateMapper doorStateMapper = new DoorStateMapper();
    RoomStateMapper roomStateMapper = new RoomStateMapper();
    ZoneStateMapper zoneStateMapper =
        new ZoneStateMapper(doorStateMapper, roomStateMapper, ventilationContextFactory);

    buildingStateMapper =
        new BuildingStateMapper(zoneStateMapper, doorStateMapper, roomStateMapper);

    sharedStateRepo = PersistenceFactory.create(buildingStateMapper);
  }

  private static void initBuildingMapRepo() {
    CampusDirectoryRepo campusDirectoryRepo = PersistenceFactory.createCampusDirectoryRepo();
    FirefighterActionFactory firefighterActionFactory = new FirefighterActionFactory();
    DoorActionFactory doorActionFactory = new DoorActionFactory();
    RoomActionFactory roomActionFactory = new RoomActionFactory();
    AlarmActionFactory alarmActionFactory = new AlarmActionFactory();
    AgentActionFactory agentActionFactory = new AgentActionFactory();
    VentilationActionFactory ventilationActionFactory = new VentilationActionFactory();
    VentilationContextFactory ventilationContextFactory = new VentilationContextFactory();
    ActionContextFactory actionContextFactory = new ActionContextFactory();
    GatheringFactory gatheringFactory = new GatheringFactory();
    InterventionFactory interventionFactory = new InterventionFactory();

    DoorAssembler doorAssembler = new DoorAssembler(doorActionFactory);
    RoomAssembler roomAssembler =
        new RoomAssembler(
            roomActionFactory,
            agentActionFactory,
            alarmActionFactory,
            campusDirectoryRepo,
            interventionFactory);
    ZoneAssembler zoneAssembler =
        new ZoneAssembler(
            roomAssembler,
            doorAssembler,
            alarmActionFactory,
            agentActionFactory,
            gatheringFactory,
            ventilationContextFactory,
            interventionFactory);
    VentilationMapper ventilationMapper = new VentilationMapper(ventilationActionFactory);
    BuildingAssembler buildingAssembler =
        new BuildingAssembler(
            zoneAssembler, ventilationMapper, firefighterActionFactory, actionContextFactory);

    sharedBuildingMapRepo =
        new BuildingMapService(buildingAssembler, buildingStateMapper, sharedStateRepo);
  }

  public static EventService createEventService() {
    if (sharedStateRepo == null) {
      initStateRepo();
    }
    if (sharedBuildingMapRepo == null) {
      initBuildingMapRepo();
    }

    return new EventService(sharedStateRepo, sharedBuildingMapRepo);
  }

  public static BuildingStateService createBuildingStateService() {
    if (sharedStateRepo == null) {
      initStateRepo();
    }
    if (sharedBuildingMapRepo == null) {
      initBuildingMapRepo();
    }

    return new BuildingStateService(sharedStateRepo, sharedBuildingMapRepo, buildingStateMapper);
  }

  public static ResetService createResetService() {
    if (sharedStateRepo == null) {
      initStateRepo();
    }

    return new ResetService(sharedStateRepo);
  }

  public static AccessService createAccessService() {

    if (sharedStateRepo == null) {
      initStateRepo();
    }
    if (sharedBuildingMapRepo == null) {
      initBuildingMapRepo();
    }

    ExternalServicesDAO externalServicesDAO = new ExternalServicesDAO();
    AccessCardsService accessCardsService = new AccessCardsService(externalServicesDAO);

    return new AccessService(sharedStateRepo, sharedBuildingMapRepo, accessCardsService);
  }
}
